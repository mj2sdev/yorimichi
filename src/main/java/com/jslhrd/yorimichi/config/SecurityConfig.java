package com.jslhrd.yorimichi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // @PreAuthorize, @PostAuthorize 사용
public class SecurityConfig {

	/**
	 * 단일 SecurityFilterChain 구성.
	 * <p>
	 * 핵심 포인트
	 * - CSRF: Thymeleaf 폼을 쓰므로 유지. 단, '지연(Deferred) 토큰'을 비활성화하여
	 * 템플릿 렌더 도중 첫 <form>에서 세션 생성하려다 응답 커밋 오류가 나는 문제를 방지.
	 * - 인가: 정적 리소스/공개 URL은 permitAll, 나머지는 인증 필요.
	 * - 폼 로그인 + 로그아웃 + 세션 관리.
	 * - OAuth2/OIDC: 클라이언트 등록이 있을 때만 동적으로 구성 (로컬 개발 시 무관).
	 */
	@Bean
	public SecurityFilterChain security(
			HttpSecurity http,
			// 소셜 설정이 없는 환경에서도 컨텍스트가 뜨도록 optional 주입
			@Autowired(required = false) ClientRegistrationRepository clients,
			@Autowired(required = false) OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2Svc,
			@Autowired(required = false) OAuth2UserService<OidcUserRequest, OidcUser> oidcSvc
	) throws Exception {

		http
				/*
				   CSRF
				   - Thymeleaf 폼 사용 시 CSRF 유지 권장.
				   - 지연 토큰 비활성화(핵심 한 줄): 렌더 전에 토큰/세션을 준비해
					 "response commit 후 세션 생성" 예외를 방지.
				 */
				.csrf(csrf -> csrf
								.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
						// (선택) fetch에서 JS로 읽기 쉬우려면 쿠키 기반으로 바꿔도 됨:
						// .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				)

				/*
				   인가(Authorization)
				   - 정적 리소스와 공개 URL 허용
				   - 나머지는 인증 필요
				 */
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								"/", "/login", "/signup", "/signup/**", "/oauth2/**", "/error",
								"/favicon.ico",
								"/assets/**", "/css/**", "/js/**", "/images/**",
								"/webjars/**",
								"/actuator/health"
						).permitAll()
						.anyRequest().authenticated()
				)

				/*
				   폼 로그인(로컬)
				   - GET /login : 커스텀 로그인 페이지
				   - POST /login: 인증 처리(필터)
				   - defaultSuccessUrl("/", false):
					 SavedRequest 있으면 복귀, 없으면 "/" 이동
				 */
				.formLogin(form -> form
						.loginPage("/login")
						.loginProcessingUrl("/login")
						.usernameParameter("email")
						.passwordParameter("password")
						.defaultSuccessUrl("/", true)
						.failureUrl("/login?error")
						.permitAll()
				)

				/*
				   로그아웃
				 */
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/")
						.deleteCookies("JSESSIONID")
						.invalidateHttpSession(true)
						.permitAll()
				)

				/*
				   세션 관리
				   - 세션 고정 공격 방지: migrateSession
				   - 동시 로그인 1개 제한
				   - 새 로그인 허용(이전 세션 무효화)
				 */
				.sessionManagement(sess -> sess
						.sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::migrateSession)
						.maximumSessions(1)
						.maxSessionsPreventsLogin(false)
				);

    /*
       소셜 로그인(OAuth2/OIDC)
       - ClientRegistrationRepository(=등록 정보)가 있을 때만 활성화
       - userInfoEndpoint().userService(...)     : 일반 OAuth2
         userInfoEndpoint().oidcUserService(...) : OIDC
     */
		if (clients != null) {
			http.oauth2Login(o -> o
					.loginPage("/login")
					.userInfoEndpoint(u -> {
						if (oauth2Svc != null) u.userService(oauth2Svc);
						if (oidcSvc != null) u.oidcUserService(oidcSvc);
					})
					.defaultSuccessUrl("/", true)
					.failureUrl("/login?oauth2_error")
			);
		}

		return http.build();
	}
}