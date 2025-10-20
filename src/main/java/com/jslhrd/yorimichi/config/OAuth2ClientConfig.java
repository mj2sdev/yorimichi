package com.jslhrd.yorimichi.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.json.gson.GsonFactory;
import com.jslhrd.yorimichi.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.InMemoryOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * OAuth2 클라이언트 등록 구성 (web 타입 JSON 전용).
 * ■ 목적
 * - api_key 테이블의 key 컬럼에 저장된 "web 타입 Google OAuth JSON"을 그대로 읽어,
 * Spring Security에서 사용할 ClientRegistration 을 런타임에 동적으로 등록한다.
 * <p>
 * ■ 전제/입력(JSON 예시)
 * {
 * "web": {
 * "client_id": "xxx",
 * "project_id":"xxx",
 * "auth_uri":"https://accounts.google.com/o/oauth2/auth",
 * "token_uri":"https://oauth2.googleapis.com/token",
 * "auth_provider_x509_cert_url": "https://www.googleapis.com/oauth2/v1/certs",
 * "client_secret": "xxx",
 * "redirect_uris": [
 * "http://localhost:8080/login/oauth2/code/google",
 * "https://YOUR.DOMAIN/login/oauth2/code/google"
 * ]
 * }
 * }
 * <p>
 * ■ SecurityConfig 연동
 * - SecurityConfig 는 ClientRegistrationRepository 를 "옵셔널 주입"으로 받고,
 * null 이 아니면 http.oauth2Login(...) 을 활성화한다.
 * - 따라서 본 클래스가 ClientRegistrationRepository 빈을 제공하면, 별도 변경 없이
 * OAuth2 로그인 기능이 자동으로 켜진다.
 * <p>
 * ■ 리다이렉트 URI 정책
 * - Spring Security 권장 템플릿: "{baseUrl}/login/oauth2/code/{registrationId}"
 * 예) 로컬에서 http://localhost:8080 으로 접근 → 콜백:
 * http://localhost:8080/login/oauth2/code/google
 * - Google Cloud Console 의 "Authorized redirect URIs"에 위 콜백 URL을 정확히 등록해야 한다.
 * (운영 배포 시 HTTPS 도메인 콜백도 함께 등록)
 * <p>
 * ■ 스코프/엔드포인트
 * - OIDC 권장 스코프: openid, profile, email
 * - authorizationUri/tokenUri 는 JSON에서 읽고,
 * userInfoUri/jwkSetUri 는 구글 표준 고정 URL을 사용한다.
 * <p>
 * ■ 실패 시 전략
 * - JSON 조회/파싱 실패 또는 필수 키 누락 시: "빈 레포지토리"를 반환한다.
 * - 이렇게 하면 애플리케이션은 정상 기동(폼 로그인/기타 기능 사용 가능),
 * 다만 /oauth2/authorization/google 접근 시 미등록으로 실패한다.
 * - 운영에서 필수라면 예외를 던져 부팅 실패 처리로 바꿔도 된다.
 */
@Configuration
@RequiredArgsConstructor
public class OAuth2ClientConfig {

	private static final String API_NAME = "google oauth2";
	private static final String API_OWNER = "mj2sdev";

	private final ApiKeyService apiKeyService;

	/**
	 * Spring Security 가 사용할 ClientRegistration 저장소 빈.
	 * <p>
	 * 반환 시나리오:
	 * - 정상: "google" 하나가 등록된 InMemoryClientRegistrationRepository
	 * - 실패: 빈 InMemoryClientRegistrationRepository (앱은 뜨되 OAuth2 로그인은 실사용 시에만 실패)
	 */
	@Bean
	public ClientRegistrationRepository clientRegistrationRepository() {

		// 1) DB에서 JSON 문자열 로드(스키마 고정: 문자열 그대로 읽는다)
		String json = apiKeyService.findApiKey(API_NAME, API_OWNER);

		if (json == null || json.isBlank()) {
			// 전략 A(관용): 빈 저장소 반환 → 앱은 정상 기동, 소셜 로그인은 시도 시만 실패
			// 전략 B(엄격): throw new IllegalStateException("...") 으로 부팅 실패 처리
			return new InMemoryClientRegistrationRepository();
		}

		try {
			// 2) GoogleClientSecrets 로 파싱 (web 전용)
			GoogleClientSecrets secrets = GoogleClientSecrets.load(
					GsonFactory.getDefaultInstance(),
					new InputStreamReader(new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)))
			);

			GoogleClientSecrets.Details web = secrets.getWeb();

			if (web == null) {
				// installed 타입이 들어온 경우: 여기서는 web만 사용할 것이므로 미등록 처리
				return new InMemoryClientRegistrationRepository();
			}

			// 3) 필수값 추출
			final String clientId = web.getClientId();
			final String clientSecret = web.getClientSecret();
			String redirectUrl = web.getRedirectUris().getFirst();

			// 5) ClientRegistration 조립
			ClientRegistration google = ClientRegistrations
					.fromIssuerLocation("https://accounts.google.com")
					.registrationId("google")
					.clientId(clientId)
					.clientSecret(clientSecret)
					.redirectUri(redirectUrl)
					.scope("openid", "profile", "email",
							"https://www.googleapis.com/auth/user.gender.read",
							"https://www.googleapis.com/auth/user.birthday.read")
					.clientName("Google")
					.build();

			// 6) InMemory 저장소로 반환(여러 Provider 로 확장 시 리스트에 추가)
			return new InMemoryClientRegistrationRepository(google);

		} catch (Exception e) {
			// JSON 파싱/인코딩 등 모든 예외 안전 처리
			// 운영에서 필수 구성이라면 여기서 throw 로 전환해도 된다.
			return new InMemoryClientRegistrationRepository();
		}
	}

	/**
	 * OAuth2AuthorizedClientService
	 * - 로그인 성공 후 발급되는 액세스/리프레시 토큰을 저장/조회하는 서비스 빈.
	 * - 여기서는 간단히 InMemory 를 사용(로컬/소규모에 적합).
	 * - 운영에서 영속화가 필요하면 JdbcOAuth2AuthorizedClientService 등으로 교체 가능.
	 */
	@Bean
	public OAuth2AuthorizedClientService authorizedClientService(ClientRegistrationRepository repo) {
		return new InMemoryOAuth2AuthorizedClientService(repo);
	}

}