package com.jslhrd.yorimichi.security;

import com.jslhrd.yorimichi.domain.SocialAccountDTO;
import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.enums.Provider;
import com.jslhrd.yorimichi.enums.Role;
import com.jslhrd.yorimichi.mapper.SocialAccountMapper;
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
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SocialUserManager {

	private final AccountService accountService;
	private final EmailNormalizer emailNormalizer;
	private final GooglePeopleClient googlePeopleClient;
	private final SocialAccountMapper socialAccountMapper;

	private static OAuth2AuthenticationException ex(String code, String desc) {
		return new OAuth2AuthenticationException(new OAuth2Error(code, desc, null), desc);
	}

	// OIDC
	@Transactional
	public OidcUser loadOidcUser(OidcUserRequest req) {


		OidcUser oidcUser = new OidcUserService().loadUser(req);
		Provider provider = Provider.fromName(req.getClientRegistration().getRegistrationId().toUpperCase(Locale.ROOT));
		Map<String, Object> claims = oidcUser.getClaims();

		String providerUserId = oidcUser.getSubject();
		String email = emailNormalizer.normalize((String) claims.get("email"));
		boolean verified = Boolean.TRUE.equals(claims.get("email_verified"));
		String name = (String) claims.get("name");
		String picture = (String) claims.get("picture");

		SocialAccountDTO socialAccount = new SocialAccountDTO(provider, providerUserId, email, verified, name, picture);
		UserDTO user = findOrProvision(socialAccount, req.getAccessToken().getTokenValue());

		Role role = (user.getRole() != null) ? user.getRole() : Role.USER;
		List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role.asAuthority()));

		AppUserPrincipal app = AppUserPrincipal.fromSocial(user, socialAccount);
		OidcIdToken idToken = oidcUser.getIdToken();
		OidcUserInfo userInfo = oidcUser.getUserInfo();

		return new AppUserPrincipalOidcAdapter(app, authorities, idToken, userInfo);
	}

	// OAuth2
	@Transactional
	public OAuth2User loadOAuth2User(OAuth2UserRequest oAuth2UserRequest) {

		Provider provider = Provider.fromName(oAuth2UserRequest.getClientRegistration().getRegistrationId().toUpperCase(Locale.ROOT));
		OAuth2AccessToken accessToken = oAuth2UserRequest.getAccessToken();

		OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(oAuth2UserRequest);
		Map<String, Object> attributes = oAuth2User.getAttributes();


		String providerUserId = (String) (attributes.getOrDefault("sub", attributes.get("id")));
		String providerEmail = emailNormalizer.normalize((String) attributes.get("email"));
		boolean verified = Boolean.TRUE.equals(attributes.getOrDefault("email_verified", false));
		String name = (String) (attributes.getOrDefault("name", attributes.get("login")));
		String picture = (String) (attributes.getOrDefault("picture", attributes.get("avatar_url")));

		SocialAccountDTO socialAccount = new SocialAccountDTO(provider, providerUserId, providerEmail, verified, name, picture);
		UserDTO user = findOrProvision(socialAccount, accessToken.getTokenValue());

		return AppUserPrincipal.fromSocial(user, socialAccount);
	}

// 콜러(예: loadOidcUser / loadOAuth2User)에서는 동일하게 호출

	/**
	 * 조회(or)신규 프로비저닝 단일 진입점.
	 * - 있으면 그대로 반환
	 * - 없으면: People API(구글일 때만) → 디폴트 보정 → INSERT → 재조회
	 */
	private UserDTO findOrProvision(SocialAccountDTO socialAccount, String accessToken) {
		return socialAccountMapper.selectByProviderAndProviderUserId(socialAccount.getProvider(), socialAccount.getProviderUserId())
				.orElseGet(() -> provisionNewUser(socialAccount, accessToken));
	}

	/**
	 * 신규 가입 전담: People API → 기본값 보정 → INSERT → 재조회
	 */
	private UserDTO provisionNewUser(SocialAccountDTO socialAccount, String accessToken) {

		Provider provider = socialAccount.getProvider();
		String providerUserId = socialAccount.getProviderUserId();

		// 1) 구글이면 People API로 gender/year 시도
		GenderYear genderYear = requireGenderYear(provider, accessToken);

		// 3) 가입(원자적 처리)
		Long userId = accountService.signupSocial(socialAccount, genderYear.gender(), genderYear.year());

		// 4) 재조회(정합성 보장)
		return socialAccountMapper.selectByProviderAndProviderUserId(provider, providerUserId)
				.orElseThrow(() -> new UsernameNotFoundException(
						"Social link failed: " + provider + "/" + providerUserId + " (userId=" + userId + ")"));
	}

	private GenderYear requireGenderYear(Provider provider, String accessToken) {

		if (provider != Provider.GOOGLE)
			throw ex("UNSUPPORTED_PROVIDER", "Google만 지원합니다.");

		if (accessToken == null || accessToken.isBlank())
			throw ex("MISSING_ACCESS_TOKEN", "People API 호출용 access token이 없습니다.");

		GooglePeopleClient.Person person = googlePeopleClient.fetchMe(accessToken);
		if (person == null)
			throw ex("PEOPLE_API_FAILED", "Google People API에서 프로필을 가져오지 못했습니다.");

		String gender = googlePeopleClient.extractGender(person);     // "M" or "F" 기대
		Integer year = googlePeopleClient.extractBirthYear(person);  // 4자리 연도 기대

		if (gender == null || gender.isBlank())
			throw ex("MISSING_GENDER", "성별 정보 동의 또는 프로필 설정이 필요합니다.");

		if (year == null)
			throw ex("MISSING_BIRTH_YEAR", "출생년도 정보 동의 또는 프로필 설정이 필요합니다.");

		return new GenderYear(gender, year);
	}

	private record GenderYear(String gender, Integer year) {
	}

}