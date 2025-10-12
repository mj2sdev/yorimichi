package com.jslhrd.yorimichi.security;

import com.jslhrd.yorimichi.domain.AccountDTO;
import com.jslhrd.yorimichi.enums.RoleName;
import com.jslhrd.yorimichi.mapper.AccountMapper;
import com.jslhrd.yorimichi.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * SocialUserService
 * -----------------
 * 소셜 로그인(OAuth2/OIDC) 시, 외부 사용자 → 내부 계정(AppUserPrincipal)으로 매핑하는 서비스입니다.
 * <p>
 * [연결 지점]
 * - SecurityConfig:
 * .oauth2Login(o -> o.userInfoEndpoint(u -> u.userService(this)))
 * 즉, provider로부터 콜백 후 userInfo 요청을 보낼 때 Spring Security가 이 서비스를 호출합니다.
 * <p>
 * [역할]
 * 1) 요청이 OIDC인지(OidcUserRequest) 일반 OAuth2인지 구분
 * - OIDC면 OidcUserService(delegate)에 위임해 표준 claims(sub, email, email_verified, name, picture)를 로딩
 * - OAuth2면 DefaultOAuth2UserService(delegate)에 위임해 provider별 속성 맵에서 공통/폴백 키로 값 추출
 * 2) (provider, sub) = “외부 자격의 절대 키”로 우리 시스템 계정 조회
 * - 없으면 최초 로그인 시점으로 간주 → AccountService.signupOrLinkSocial(...) 호출해서
 * root → user → social_account를 생성/연결(멱등성: UNIQUE(provider, sub)) → 재조회
 * 3) AccountDTO(최소 정보)로부터 AppUserPrincipal(슬림: UserDetails + OAuth2User) 생성
 * - 소셜 경로는 password 미사용(null)
 * - enabled / accountNonLocked는 DB 상태(deleted_at, blinded_at) 기반의 파생값
 * <p>
 * [주의점/베스트프랙티스]
 * - sub는 “절대 키”입니다. 없으면 인증 중단(OAuth2AuthenticationException)합니다.
 * - provider 문자열은 Locale-민감하니 .toUpperCase(Locale.ROOT)로 표준화합니다.
 * - Optional 체인은 가독성/디버깅을 위해 if(isEmpty()) 재조회 패턴으로 기술(람다 final 제약도 회피).
 * - AppUserPrincipal에는 무거운 클레임/토큰을 싣지 않습니다(세션 슬림/보안).
 * - 최초 로그인 동시에 “마이그레이션/링크” 시나리오(이미 로컬 계정 존재)도 AccountService에서 처리합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SocialUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final AccountMapper accountMapper;   // 인증 전용 조회/로그 기록 등(쿼리 최소화)
	private final AccountService accountService; // 최초 소셜 로그인 link/signup 비즈니스(원자 처리)

	/**
	 * OAuth2/OIDC 로그인 시, Spring Security가 호출하는 진입점.
	 * - 외부 사용자 정보(OIDC claims or OAuth2 attributes)를 로딩 → 내부 계정 매핑/생성 → Principal 반환
	 */
	@Override
	@Transactional // 최초 로그인 시 “링크/가입”까지 수행하므로 트랜잭션(원자성) 필수
	public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {

		/* 1) 어떤 provider인지 식별
		 *    - client.registrationId는 application.yml의 spring.security.oauth2.client.registration.<id> 키
		 *    - 로케일 독립 대문자 표준화(예: "GOOGLE", "GITHUB")
		 */
		final String provider = req.getClientRegistration()
				.getRegistrationId()
				.toUpperCase(Locale.ROOT);

		/* 2) 외부 사용자 로딩 (OIDC vs OAuth2)
		 *    - 공통으로 필요: sub(절대 키), email(있을 수 있음), email_verified(있을 수 있음), name, picture
		 *    - 다양한 provider를 고려해 안전한 폴백 전략을 사용
		 */
		String sub;             // 공급자 고유 사용자 ID (OIDC: 'sub'는 표준 필수)
		String email;           // 공급자 측 이메일 (비공개일 수 있음)
		boolean verified = false; // 이메일 인증 여부(없는 provider도 있음)
		String name = null;     // 표시명
		String picture = null;  // 아바타 URL

		if (req instanceof OidcUserRequest oidcReq) {
			// ---- OIDC 경로 (예: Google OpenID Connect) ----
			// 표준 OIDC 서비스에 위임하여 id_token/userinfo 기반으로 프로필 로드
			OidcUserService delegate = new OidcUserService();
			OidcUser oidc = delegate.loadUser(oidcReq);

			// OIDC 표준 claims에서 값 추출(키는 표준 스펙)
			Map<String, Object> claims = oidc.getClaims();
			sub = oidc.getSubject();                           // 표준 고유 ID
			email = (String) claims.get("email");                // 이메일(없을 수 있음)
			Boolean emailVerified = (Boolean) claims.get("email_verified");
			verified = emailVerified != null && emailVerified;      // null-safe
			name = (String) claims.get("name");                 // 표시명(없을 수 있음)
			picture = (String) claims.get("picture");              // 아바타(없을 수 있음)

		} else {
			// ---- 일반 OAuth2 경로 (예: GitHub, Naver, Kakao 등) ----
			// 표준 OAuth2 서비스에 위임하여 access_token으로 사용자 attributes 로드
			DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
			OAuth2User oauth = delegate.loadUser(req);

			Map<String, Object> attr = oauth.getAttributes();
			// 공급자별 속성 키가 다름 → 최대공약수 + 폴백 키 전략
			sub = (String) (attr.getOrDefault("sub", attr.get("id")));     // OIDC 'sub' 또는 일반 'id'
			email = (String) attr.get("email");                               // 없을 수 있음
			name = (String) (attr.getOrDefault("name", attr.get("login")));  // GitHub: login
			picture = (String) (attr.getOrDefault("picture", attr.get("avatar_url")));
			// verified는 제공 안 할 수 있음 → 필요 시 provider별 변환 규칙 추가
		}

		/* 3) sub는 매핑의 “절대 키”
		 *    - 누락 시 더 진행할 수 없음 → 즉시 인증 실패 처리
		 */
		if (sub == null || sub.isBlank()) {
			throw new OAuth2AuthenticationException(
					new OAuth2Error("invalid_user_info"), // 표준이 아니더라도 식별 가능한 코드
					"Missing subject (sub) from provider: " + provider
			);
		}

		if (log.isDebugEnabled()) {
			log.debug("OAuth2/OIDC parsed provider={} sub={} email={} verified={} name={}",
					provider, sub, email, verified, name);
		}

		/* 4) (provider, sub)로 내부 계정 조회
		 *    - Optional 사용(단건 조회 관례)
		 *    - 없으면 “최초 로그인”으로 판단 → link/signup 수행 후 재조회(정합성 보장)
		 *    - 멱등성: social_account (provider, provider_user_id) UNIQUE 제약으로 보장
		 */
		Optional<AccountDTO> opt = accountMapper.selectByProviderAndSub(provider, sub);

		if (opt.isEmpty()) {
			// 4-1) 링크/가입: 이메일/표시명/아바타는 제공되는 범위 내에서만 사용
			Long userId = accountService.signupOrLinkSocial(provider, sub, email, verified, name, picture);

			// 4-2) 재조회(동시성/정합성 보장). 비어있으면 데이터 이상 → 예외
			opt = accountMapper.selectByProviderAndSub(provider, sub);
			if (opt.isEmpty()) {
				throw new UsernameNotFoundException(
						"Social link failed: " + provider + "/" + sub + " (userId=" + userId + ")"
				);
			}
		}

		// 여기서 Optional은 non-empty 보장
		AccountDTO a = opt.get();

		/* 5) ROLE 방어
		 *    - 정상 입력이면 null이 아니어야 하나, 데이터 손상/수작업 레코드 등 예외 대비
		 *    - 기본값: USER
		 */
		RoleName role = (a.getRole() != null) ? a.getRole() : RoleName.USER;

		/* 6) AppUserPrincipal(슬림) 생성
		 *    - 소셜 경로는 password 검증을 하지 않으므로 세션에 보관하지 않음(null)
		 *    - enabled / accountNonLocked 는 AccountDTO에서 파생(또는 쿼리 파생)
		 *    - 권한은 RoleName → "ROLE_..." 문자열로 변환해 SimpleGrantedAuthority 부여
		 */
		var principal = AppUserPrincipal.builder()
				.userId(a.getId())
				.email(a.getEmail())
				.password(null) // 소셜: 내부 비번 검증 없음 → 세션 미보관
				.authorities(List.of(new SimpleGrantedAuthority(role.asAuthority())))
				.enabled(a.isEnabled())
				.accountNonLocked(a.isNonLocked())
				.provider(provider)
				.providerUserId(sub)
				.build();

		/* 7) (선택) 로그인 기록
		 *    - 보안 이벤트/감사 로그가 필요하면 성공 시각을 갱신
		 *    - 트래픽이 매우 큰 경우 비동기로 전환 고려
		 */
		// accountMapper.touchLastLoginAt(a.getId());
		// accountMapper.touchSocialLastLoginAt(a.getId(), provider);

		return principal;
	}
}