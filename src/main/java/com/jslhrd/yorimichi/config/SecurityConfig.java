package com.jslhrd.yorimichi.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
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

	@Bean
	@Order(1)
	SecurityFilterChain wellKnownSecurityChainFilter(HttpSecurity http) throws Exception {
		http
				.securityMatcher("/.well-known/**")
				.authorizeHttpRequests(a -> a.anyRequest().permitAll())
				.csrf(csrf -> csrf.disable())
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.httpBasic(b -> b.disable())
				.formLogin(f -> f.disable())
				.logout(l -> l.disable());
		return http.build();
	}

	/*
	 * API 체인
	 * */
	@Bean
	@Order(2)
	SecurityFilterChain apiSecurityChainFilter(HttpSecurity http) throws Exception {
		http
				.securityMatcher("/api/**")
				.csrf(csrf -> csrf.disable())
				.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(HttpMethod.GET, "/api/categories", "/api/search/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/stations").permitAll()
						// 나머지 API 는 인증 필요
						.anyRequest().authenticated()
				)
				// 실패 시 JSON 401/403 으로
				.exceptionHandling(e -> e
						.authenticationEntryPoint((req, res, ex) -> {
							res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
							res.setContentType("application/json");
							res.getWriter().write("{\"error\": \"UNAUTHORIZED\"}");
						})
						.accessDeniedHandler((req, res, ex) -> {
							res.setStatus(HttpServletResponse.SC_FORBIDDEN);
							res.setContentType("application/json");
							res.getWriter().write("{\"error\": \"FORBIDDEN\"}");
						})
				);

		// (선택) JWT 사용시
		// http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

		return http.build();
	}

	/*
	 * 웹(SSR) 체인
	 * */
	@Bean
	@Order(3)
	public SecurityFilterChain webSecurityChainFilter(
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
								"/assets/**", "/css/**", "/js/**", "/images/**","/feed/**" , "/api/**",
								"/webjars/**",  "/search/**", "/store/**",
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
						.defaultSuccessUrl("/", false)
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
				.sessionManagement(sm -> sm
						.sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::migrateSession)
						.maximumSessions(1)
						.maxSessionsPreventsLogin(false)
				);
		http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**")); 
    /*
       소셜 로그인(OAuth2/OIDC)
       - ClientRegistrationRepository(=등록 정보)가 있을 때만 활성화
       - userInfoEndpoint().userService(...)     : 일반 OAuth2
         userInfoEndpoint().oidcUserService(...) : OIDC
     */
		if (clients != null) {
			http.oauth2Login(oauth2 -> oauth2
					.loginPage("/login")
					.userInfoEndpoint(u -> {
						if (oauth2Svc != null) u.userService(oauth2Svc);
						if (oidcSvc != null) u.oidcUserService(oidcSvc);
					})
					.defaultSuccessUrl("/", false)
					.failureUrl("/login?oauth2_error")
			);
		}

		return http.build();
	}
}