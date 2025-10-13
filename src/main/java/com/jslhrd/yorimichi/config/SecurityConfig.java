package com.jslhrd.yorimichi.config;

import com.jslhrd.yorimichi.security.LocalUserDetailsManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security의 최상위 보안 설정.
 * <p>
 * 구성 개요
 * - 인증(Authentication): 폼 로그인(로컬) + OAuth2/OIDC(소셜)
 * - 인가(Authorization): 공개/보호 URL 분리
 * - CSRF: 웹 폼 보호 유지, REST API(/api/**)는 토큰 인증 전제하에 예외 처리
 * - 세션: 세션 고정 보호, 동시 로그인 1개 제한
 * - 예외: API는 JSON(401/403), 웹은 페이지 리다이렉트
 * <p>
 * 변경/확장 시 주의
 * - 정적 리소스는 가능한 permitAll() 권장, web.ignoring()은 최소화(보안 훅 완전 우회)
 * - OAuth2 vs OIDC: 서로 다른 제네릭 훅(userService, oidcUserService)에 각기 바인딩해야 함
 * - DaoAuthenticationProvider를 Bean으로 명시 등록(PasswordEncoder 연동 확실히)
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // @PreAuthorize, @PostAuthorize 활성화
@RequiredArgsConstructor
public class SecurityConfig {

	// 로컬(폼) 로그인 시 UserDetails 로딩 담당 (이메일 → 사용자 조회)
	private final LocalUserDetailsManager localUserDetailsManager;

	// 소셜(OAuth2/OIDC) 로그인 시 외부 사용자 → 내부 계정 매핑/생성 담당
	private final OAuth2UserService<OAuth2UserRequest, OAuth2User> socialOAuth2UserService;
	private final OAuth2UserService<OidcUserRequest, OidcUser> socialOidcUserService;
	private final PasswordEncoder passwordEncoder;

	/**
	 * WebSecurityCustomizer
	 * <p>
	 * - 보안 필터 체인 자체를 완전히 "우회"할 경로 지정.
	 * - 인증/인가/로깅/CSRF 등 모든 보안 훅이 적용되지 않으므로 최소화가 원칙.
	 * - 정적 리소스/헬스체크 등만 등록 권장.
	 * - 대안: authorizeHttpRequests().requestMatchers(...).permitAll() (필터는 타되 접근만 허용)
	 */
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().requestMatchers(
				"/favicon.ico",
				"/assets/**", "/css/**", "/js/**", "/images/**",
				"/webjars/**",
				"/actuator/health"
		);
	}

	/**
	 * 보안 필터 체인(핵심 설정)
	 * <p>
	 * - 이 메서드에서 대부분의 보안 정책을 선언적으로 구성한다.
	 * - http.build() 시 체인이 동결되어 애플리케이션에 적용.
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(
			HttpSecurity http,
			// 클라이언트 등록이 없을 때 oauth2Login() 스킵 (개발, 로컬에서 유용)
			@Autowired(required = false) ClientRegistrationRepository clients,
			@Autowired(required = false) OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2Svc,
			@Autowired(required = false) OAuth2UserService<OidcUserRequest, OidcUser> oidcSvc
	) throws Exception {

		http
				/* =========================
				   CSRF 설정
				   - 웹 폼(세션 기반)은 CSRF 보호 유지(기본).
				   - REST API("/api/**")는 일반적으로 세션을 쓰지 않고 토큰 인증(JWT 등)을 사용 → CSRF 예외로 둠.
				   - 주의: 프론트가 쿠키+세션을 쓰는 API라면, CSRF를 무조건 끄지 말고 전략을 재검토.
				   ========================= */
				.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))

				/* =========================
				   인가(Authorization) 규칙
				   - 공개 경로: 홈/로그인/회원가입/소셜 엔드포인트/에러 → permitAll
				   - 그 외 모든 경로: 인증 필요
				   - 세밀한 권한(Role) 제어는 메서드 보안(@PreAuthorize)와 함께 조합 사용.
				   ========================= */
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/", "/login", "/signup", "/oauth2/**", "/error").permitAll()
						.anyRequest().authenticated()
				)

				/* =========================
				   폼 로그인(로컬)
				   - /login 페이지를 커스텀 로그인 페이지로 사용.
				   - usernameParameter, passwordParameter는 폼 input name과 동일해야 함.
				   - defaultSuccessUrl("/", false):
					 1) SavedRequest가 있으면 원래 요청으로 복귀
					 2) 없으면 "/"로 이동
				   - 실패 시에는 /login?error로 리다이렉트(메시지는 프론트에서 처리).
				   ========================= */
				.formLogin(form -> form
						.loginPage("/login")
						.loginProcessingUrl("/login")   // POST /login 처리 엔드포인트
						.usernameParameter("email")     // 폼의 name="email"
						.passwordParameter("password")  // 폼의 name="password"
						.defaultSuccessUrl("/", false)
						.failureHandler((req, res, ex) -> res.sendRedirect("/login?error"))
						.permitAll()
				)

				/* =========================
				   소셜 로그인(OAuth2/OIDC)
				   - 동일한 /login 페이지에서 시작 (UI는 소셜 버튼 제공).
				   - userInfoEndpoint().userService(...)  : OAuth2 공급자 (attributes 기반)
					 userInfoEndpoint().oidcUserService(...): OIDC 공급자 (id_token/claims 기반)
				   - 성공/실패 핸들러에서 후처리(마지막 로그인 시각 갱신, 최초 가입 분기 등) 가능.
				   ========================= */
				/*.oauth2Login(oauth -> oauth
						.loginPage("/login")
						.userInfoEndpoint(u -> u
								.userService(socialOAuth2UserService)     // OAuth2
								.oidcUserService(socialOidcUserService)   // OIDC
						)
						.defaultSuccessUrl("/", false)
						.failureHandler((req, res, ex) -> res.sendRedirect("/login?oauth2_error"))
				)*/

				/* =========================
				   로그아웃
				   - 기본은 POST /logout (CSRF 보호 하).
				   - logoutUrl("/logout"): 엔드포인트 지정(POST 권장).
				   - deleteCookies("JSESSIONID"): 세션 쿠키 제거.
				   - invalidateHttpSession(true): 서버측 세션 무효화.
				   - GET 로그아웃 허용은 보안상 비권장(필요시 별도 RequestMatcher 지정).
				   ========================= */
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/")
						.deleteCookies("JSESSIONID")
						.invalidateHttpSession(true)
						.permitAll()
				)

				/* =========================
				   세션 관리
				   - sessionFixation().migrateSession(): 세션 고정 공격 방지(인증 성공 시 새 세션 발급).
				   - maximumSessions(1): 동시 로그인 1개로 제한.
				   - maxSessionsPreventsLogin(false): 새 로그인 허용(이전 세션 무효화).
					 (true로 바꾸면 기존 세션이 우선, 새 로그인 거부)
				   ========================= */
				.sessionManagement(sess -> sess
						.sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::migrateSession)
						.maximumSessions(1)
						.maxSessionsPreventsLogin(false)
				)

				/* =========================
				   예외 처리
				   - 인증되지 않은 요청(401):
					 * API(/api/**): JSON 본문과 함께 401 반환
					 * 웹 페이지: /login으로 리다이렉트
				   - 권한 부족(403):
					 * API(/api/**): JSON 본문과 함께 403 반환
					 * 웹 페이지: /403 페이지로 리다이렉트
				   - 보안 예외는 필터 체인에서 발생 → @ControllerAdvice 범위 바깥임에 유의.
				   ========================= */
				.exceptionHandling(ex -> ex
						.authenticationEntryPoint((request, response, authEx) -> {
							String uri = request.getRequestURI();
							if (uri.startsWith("/api/")) {
								response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
								response.setContentType("application/json;charset=UTF-8");
								response.getWriter().write("""
										    {"code":"UNAUTHORIZED","message":"로그인이 필요합니다."}
										""");
							} else {
								// SavedRequest가 있으면 기본 동작으로 로그인 페이지로
								response.sendRedirect("/login");
							}
						})
						.accessDeniedHandler((request, response, exDenied) -> {
							String uri = request.getRequestURI();
							if (uri.startsWith("/api/")) {
								response.setStatus(HttpServletResponse.SC_FORBIDDEN);
								response.setContentType("application/json;charset=UTF-8");
								response.getWriter().write("""
										    {"code":"FORBIDDEN","message":"접근 권한이 없습니다."}
										""");
							} else {
								response.sendRedirect("/403");
							}
						})
				);

		// 등록 정보가 있을 때만 소셜 로그인 구성
		if (clients != null) {
			http.oauth2Login(o -> o
					.loginPage("/login")
					.userInfoEndpoint(u -> {
						if (oauth2Svc != null) u.userService(oauth2Svc);
						if (oidcSvc != null) u.oidcUserService(oidcSvc);
					})
					.defaultSuccessUrl("/", false)
					.failureUrl("/login?oauth2_error")
			);
		}

		// DaoAuthenticationProvider를 명시 등록
		// - 내부적으로 UserDetailsService + PasswordEncoder 조합으로 폼 로그인 인증 수행
		// - 아래 Bean에서 PasswordEncoder 연동을 보장하므로, http.userDetailsService(...) 중복 설정 불필요
		http.authenticationProvider(daoAuthenticationProvider());

		return http.build();
	}

	/**
	 * DaoAuthenticationProvider
	 * <p>
	 * - 이메일/비밀번호(폼 로그인) 인증 처리에 사용.
	 * - UserDetailsService로 사용자 조회 → PasswordEncoder로 비밀번호 일치 여부 검증.
	 * - 실패 시 BadCredentialsException 등 발생, 실패 핸들러로 전달.
	 */
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider p = new DaoAuthenticationProvider();
		p.setUserDetailsService(localUserDetailsManager);
		p.setPasswordEncoder(passwordEncoder);
		return p;
	}
}