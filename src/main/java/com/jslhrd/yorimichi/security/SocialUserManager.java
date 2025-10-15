package com.jslhrd.yorimichi.security;

import com.jslhrd.yorimichi.domain.SocialAccountDTO;
import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.enums.Provider;
import com.jslhrd.yorimichi.enums.Role;
import com.jslhrd.yorimichi.mapper.AccountMapper;
import com.jslhrd.yorimichi.service.AccountService;
import com.jslhrd.yorimichi.util.EmailNormalizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * SocialUserManager
 * -----------------
 * OAuth2/OIDC 로그인 시, 외부 사용자 정보를 내부 시스템 사용자(UserDTO)로
 * 매핑(조회·프로비저닝)하고, Spring Security가 기대하는 Principal을 반환합니다.
 *
 * <p>연결 지점 (SecurityConfig):
 * <pre>
 * .oauth2Login(o -> o
 *   .userInfoEndpoint(u -> u
 *     .userService(socialOAuth2UserService(this))     // OAuth2 공급자(GitHub/Naver/Kakao 등)
 *     .oidcUserService(socialOidcUserService(this))   // OIDC 공급자(Google/Apple 등)
 *   )
 * )
 * </pre>
 *
 * <p>핵심 정책:
 * <ul>
 *   <li>(provider, providerUserId(sub))를 외부 자격의 "절대 키"로 사용</li>
 *   <li>최초 로그인 시 멱등 프로비저닝(UNIQUE(provider, provider_user_id)로 보장)</li>
 *   <li>이메일은 소문자/trim 정규화; 미제공 가능성 고려</li>
 *   <li>세션 슬림 전략: OIDC 토큰/클레임은 DefaultOidcUser에만 담고, AppUserPrincipal에는 최소 정보만</li>
 * </ul>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SocialUserManager {

	private final AccountMapper accountMapper;   // 인증 전용 조회/집계/로그 업데이트 등
	private final AccountService accountService; // 최초 소셜 로그인 시 link/signup 처리(트랜잭션)
	private final EmailNormalizer emailNormalizer;

	// ==========================================================
	// OAuth2 전용 (예: GitHub, Kakao, Naver 등 - OIDC 아닌 공급자)
	// ==========================================================

	/**
	 * OAuth2 사용자 로딩 및 내부 계정 매핑.
	 *
	 * <p>흐름:
	 * 1) registrationId로 공급자 식별(대문자 표준화)
	 * 2) DefaultOAuth2UserService로 attributes 조회
	 * 3) providerUserId(=sub or id) 추출 실패 시 인증 중단
	 * 4) (provider, providerUserId)로 내부 사용자 조회; 없으면 프로비저닝
	 * 5) AppUserPrincipal(슬림) 반환 → SecurityContext에 저장
	 *
	 * @param req OAuth2UserRequest (access_token 포함)
	 * @return AppUserPrincipal (UserDetails + OAuth2User)
	 * @throws OAuth2AuthenticationException 외부 정보 불충분/이상 시
	 */
	@Transactional // 최초 로그인 시 link/signup 발생 가능 → 원자성 보장
	public OAuth2User loadOAuth2User(OAuth2UserRequest req) {
		// 1) 공급자 식별 (application.yml의 registrationId를 대문자 표준화)
		final Provider provider = Provider.fromName(
				req.getClientRegistration().getRegistrationId().toUpperCase(Locale.ROOT));

		// 2) 표준 OAuth2 delegate로 사용자 attributes 조회
		OAuth2User oauth = new DefaultOAuth2UserService().loadUser(req);
		Map<String, Object> attr = oauth.getAttributes();

		// 3) 공급자별 키 상이 → 최대공약수 기반 폴백(sub or id)
		String providerUserId = (String) (attr.getOrDefault("sub", attr.get("id")));
		String email = emailNormalizer.normalize((String) attr.get("email"));                       // 미제공 가능
		String name = (String) (attr.getOrDefault("name", attr.get("login")));     // GitHub: login
		if (name != null) name = name.trim();
		String picture = (String) (attr.getOrDefault("picture", attr.get("avatar_url")));
		boolean verified = false; // 필요 시 공급자별 검증 플래그 매핑(예: 'verified_email') 추가

		// 외부 고유 식별자 누락 시 더 진행 불가 → 인증 실패
		if (providerUserId == null || providerUserId.isBlank()) {
			throw new OAuth2AuthenticationException(new OAuth2Error("invalid_user_info"),
					"Missing subject/id from provider: " + provider);
		}

		// 4) 사용자 조회/프로비저닝 (멱등)
		UserDTO user = findOrProvision(provider, providerUserId, email, verified, name, picture);

		// 5) 슬림 Principal 반환 (세션에 필요한 최소 정보만)
		return AppUserPrincipal.fromSocial(user,
				new SocialAccountDTO(/* Provider enum */ provider,
						/* providerUserId */ providerUserId,
						/* providerEmail */ email,
						/* emailVerified */ verified,
						/* displayName */ name,
						/* avatarUrl */ picture));
	}

	// ==========================================================
	// OIDC 전용 (예: Google/Apple 등 - 표준 OIDC 공급자)
	// ==========================================================

	/**
	 * OIDC 사용자 로딩 및 내부 계정 매핑.
	 *
	 * <p>흐름:
	 * 1) OidcUserService 델리게이트로 id_token/userinfo 로딩
	 * 2) 표준 클레임(sub/email/email_verified/name/picture) 추출
	 * 3) sub 누락 시 인증 중단
	 * 4) (provider, sub)로 내부 사용자 조회; 없으면 프로비저닝
	 * 5) DefaultOidcUser로 반환 (OIDC 표준 타입; nameAttributeKey = "sub")
	 *
	 * <p>주의:
	 * - 일부 공급자는 userInfo가 null일 수 있으므로 생성자 분기 필요.
	 * - AppUserPrincipal을 OidcUser로 쓰고 싶다면(AppUserPrincipal 단일화) 별도 구현이 필요합니다.
	 */
	@Transactional
	public OidcUser loadOidcUser(OidcUserRequest req) {
		// 1) 표준 OIDC 델리게이트 (id_token, userinfo 처리)
		OidcUser oidc = new OidcUserService().loadUser(req);

		// 2) 공급자 식별(대문자 표준화) 및 클레임 추출
		final Provider provider = Provider.fromName(
				req.getClientRegistration().getRegistrationId().toUpperCase(Locale.ROOT));

		Map<String, Object> claims = oidc.getClaims();
		String sub = oidc.getSubject();                          // 필수
		String email = emailNormalizer.normalize((String) claims.get("email"));  // 미제공 가능
		Boolean emailVerified = (Boolean) claims.get("email_verified");
		boolean verified = emailVerified != null && emailVerified;
		String name = (String) claims.get("name");               // 미제공 가능
		String picture = (String) claims.get("picture");         // 미제공 가능

		if (sub == null || sub.isBlank()) {
			throw new OAuth2AuthenticationException(new OAuth2Error("invalid_user_info"),
					"Missing subject (sub) from OIDC provider: " + provider);
		}

		// 3) 사용자 조회/프로비저닝 (멱등)
		UserDTO user = findOrProvision(provider, sub, email, verified, name, picture);

		// 4) 권한 구성 (Role이 null이면 USER 기본값)
		Role role = (user.getRole() != null) ? user.getRole() : Role.USER;
		var authorities = List.of(new SimpleGrantedAuthority(role.asAuthority()));

		// 5) OIDC 규약 타입으로 반환 (nameAttributeKey = "sub")
		//    - userInfo가 null일 수 있으므로 안전 분기
		if (oidc.getUserInfo() != null) {
			return new DefaultOidcUser(authorities, oidc.getIdToken(), oidc.getUserInfo(), "sub");
		}
		return new DefaultOidcUser(authorities, oidc.getIdToken(), "sub");
	}

	/**
	 * 내부 사용자 조회/프로비저닝(멱등).
	 *
	 * <p>정책:
	 * - (provider, providerUserId)로 단건 조회
	 * - 없으면 SocialAccountDTO를 생성하여 link/signup 수행
	 * - UNIQUE(provider, provider_user_id) 제약으로 동시성/중복을 DB 레벨에서 차단
	 * - 성공 후 재조회해 정합성을 보장
	 *
	 * @param provider       외부 공급자
	 * @param providerUserId 외부 공급자의 사용자 고유 ID (OIDC: sub)
	 * @param email          외부 제공 이메일(없을 수 있음; 로컬 이메일과 다를 수 있음)
	 * @param verified       이메일 검증 여부(공급자 기준)
	 * @param name           표시명(없을 수 있음)
	 * @param picture        아바타 URL(없을 수 있음)
	 * @return UserDTO        인증·인가에 필요한 최소 정보가 채워진 사용자
	 * @throws UsernameNotFoundException 링크/가입 후에도 조회 실패 시 (데이터 이상)
	 */
	private UserDTO findOrProvision(Provider provider, String providerUserId, String email,
	                                boolean verified, String name, String picture) {
		// 1) 우선 조회
		Optional<UserDTO> found = accountMapper.selectByProviderAndSub(provider, providerUserId);
		if (found.isPresent()) return found.get();

		// 2) 없으면 link/signup (AccountService 내부에서 root/user/social_account 원자적 처리)
		SocialAccountDTO sa = SocialAccountDTO.builder()
				.provider(provider)
				.providerUserId(providerUserId)
				.providerEmail(email)
				.emailVerified(verified)
				.displayName(name)
				.avatarUrl(picture)
				.build();

		Long userId = accountService.signupSocial(sa);

		// 3) 재조회(동시성/정합성 보장). 비어있으면 데이터 이상 → 예외
		return accountMapper.selectByProviderAndSub(provider, providerUserId)
				.orElseThrow(() -> new UsernameNotFoundException(
						"Social link failed: " + provider + "/" + providerUserId + " (userId=" + userId + ")"));
	}
}