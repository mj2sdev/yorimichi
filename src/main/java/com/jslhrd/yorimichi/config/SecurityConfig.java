package com.jslhrd.yorimichi.config;

import com.jslhrd.yorimichi.security.LocalUserDetailsManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
 * - CSRF: 웹 폼 보호 유지, REST API(/api/**)는 토큰 인증 전제하에 예외
 * - 세션: 세션 고정 보호, 동시 로그인 1개 제한
 * - 예외: API는 JSON(401/403), 웹은 페이지 리다이렉트
 * <p>
 * 주의
 * - 정적 리소스는 가급적 permitAll()로 허용하고 web.ignoring()은 최소화(필터 완전 우회라 로깅/보호도 스킵됨).
 * - OAuth2 vs OIDC: userService(일반 OAuth2)와 oidcUserService(OIDC)는 제네릭이 달라 각각 바인딩.
 * - DaoAuthenticationProvider를 Bean으로 명시 등록해 PasswordEncoder 확실히 연동.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // @PreAuthorize, @PostAuthorize 사용
@RequiredArgsConstructor
public class SecurityConfig {

	/**
	 * 폼 로그인 시 이메일로 사용자 로딩
	 */
	private final LocalUserDetailsManager localUserDetailsManager;

	/**
	 * 소셜(OAuth2) 사용자 로딩 (attributes 기반)
	 */
	private final OAuth2UserService<OAuth2UserRequest, OAuth2User> socialOAuth2UserService;

	/**
	 * 소셜(OIDC) 사용자 로딩 (id_token/claims 기반)
	 */
	private final OAuth2UserService<OidcUserRequest, OidcUser> socialOidcUserService;

	/**
	 * 비밀번호 해시/검증용
	 */
	private final PasswordEncoder passwordEncoder;

	/**
	 * WebSecurityCustomizer
	 * <p>
	 * - 보안 "필터 체인 자체"를 완전히 우회할 경로를 정의합니다.
	 * - 여기 들어간 경로는 인증/인가/로깅/CSRF 등 보안 훅이 전혀 타지 않습니다(매우 강력).
	 * - 따라서 정말 필요한 최소(정적 리소스, 헬스체크 등)만 넣으세요.
	 * - 대안: authorizeHttpRequests().requestMatchers(...).permitAll()
	 * (필터는 거치면서 접근만 허용 → 로깅/보안 기능 일부 유지)
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
	 * SecurityFilterChain
	 * <p>
	 * - 보안 정책의 핵심을 선언합니다. http.build() 시 체인이 동결되어 적용됩니다.
	 * - 아래에서 oauth2Login()은 ClientRegistrationRepository가 있을 때만 동적으로 추가합니다
	 * (로컬 개발에서 소셜 설정이 비어 있어도 앱이 뜨도록 하기 위한 안전장치).
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(
			HttpSecurity http,
			// ⬇ 아래 3개는 optional 주입 (없으면 null) → 소셜 설정이 없을 때 oauth2Login 블록 자체를 스킵
			@Autowired(required = false) ClientRegistrationRepository clients,
			@Autowired(required = false) OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2Svc,
			@Autowired(required = false) OAuth2UserService<OidcUserRequest, OidcUser> oidcSvc
	) throws Exception {

		http
				/* =========================
				   CSRF
				   - 폼 기반 웹은 기본 보호 유지.
				   - 토큰 기반 REST API(/api/**)는 일반적으로 세션 미사용 → 예외 처리.
				   - 쿠키+세션으로 API를 때린다면 무조건 예외를 주지 말고 별도 전략 고민 필요.
				   ========================= */
				.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))

				/* =========================
				   인가(Authorization)
				   - 공개 경로는 permitAll
				   - 나머지는 인증 필요
				   - 세밀한 Role 제어는 @PreAuthorize와 조합
				   ========================= */
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/", "/login", "/signup", "/oauth2/**", "/error").permitAll()
						.anyRequest().authenticated()
				)

				/* =========================
				   폼 로그인(로컬)
				   - GET /login : 커스텀 로그인 페이지(컨트롤러/뷰가 렌더링)
				   - POST /login: 인증 처리 엔드포인트(시큐리티 필터가 처리)
				   - username/password 파라미터명은 폼 input name과 일치시킬 것
				   - defaultSuccessUrl("/", false): SavedRequest 있으면 복귀, 없으면 "/"
				   ========================= */
				.formLogin(form -> form
						.loginPage("/login")            // 커스텀 로그인 페이지 URL
						.loginProcessingUrl("/login")   // 인증 처리 엔드포인트(POST)
						.usernameParameter("email")
						.passwordParameter("password")
						.defaultSuccessUrl("/", false)
						.failureHandler((req, res, ex) -> res.sendRedirect("/login?error"))
						.permitAll()
				)

				/* =========================
				   로그아웃
				   - 기본 POST /logout (CSRF 보호)
				   - 세션 쿠키 삭제 + 서버 세션 무효화
				   - GET 로그아웃은 보안상 권장하지 않음(필요시 별도 RequestMatcher)
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
				   - migrateSession: 세션 고정 공격 방지(인증 성공 시 새 세션 발급)
				   - maximumSessions(1): 동시 로그인 1개 제한
				   - maxSessionsPreventsLogin(false): 새 로그인 허용(이전 세션 무효화)
				   ========================= */
				.sessionManagement(sess -> sess
						.sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::migrateSession)
						.maximumSessions(1)
						.maxSessionsPreventsLogin(false)
				)

				/* =========================
				   예외 처리
				   - 401(미인증): API는 JSON, 웹은 /login으로 리다이렉트
				   - 403(권한없음): API는 JSON, 웹은 /403 페이지
				   - 보안 예외는 필터 체인에서 발생하므로 @ControllerAdvice 외부
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
								response.sendRedirect("/login");
							}
						})
						.accessDeniedHandler((request, response, denied) -> {
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

        /* =========================
           소셜 로그인(OAuth2/OIDC)
           - ClientRegistrationRepository(=등록 정보)가 있을 때만 구성
           - userInfoEndpoint().userService(...)     : 일반 OAuth2
             userInfoEndpoint().oidcUserService(...) : OIDC
           - 성공/실패 후처리는 필요에 따라 핸들러로 대체 가능
           ========================= */
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

		return http.build();
	}

}