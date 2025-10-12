package com.jslhrd.yorimichi.config;

import com.jslhrd.yorimichi.security.LocalUserDetailsManager;
import com.jslhrd.yorimichi.security.SocialUserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // @PreAuthorize, @PostAuthorize 사용 허용
@RequiredArgsConstructor
public class SecurityConfig {

	// 폼 로그인(로컬) 시 사용자 로딩을 담당하는 서비스
	private final LocalUserDetailsManager localUserDetailsManager;

	// 소셜(OIDC) 로그인 시 사용자 매핑/생성을 담당하는 서비스
	private final SocialUserService socialUserService;

	/**
	 * 비밀번호 인코더
	 * - 로컬 유저 가입/로그인 시 BCrypt 해시를 사용합니다.
	 * - DB에 저장된 비밀번호는 반드시 BCrypt 해시여야 합니다.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * WebSecurityCustomizer
	 * - 보안 필터 체인 자체를 완전히 우회할 경로를 지정합니다.
	 * - 정적 리소스, 헬스체크 등 보안 처리가 불필요한 요청만 넣는 것이 베스트 프랙티스입니다.
	 * - 주의: 여기 지정된 경로는 인증/인가/로깅/CSRF 등 모든 보안 훅을 타지 않습니다.
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
	 * - 스프링 시큐리티의 핵심 설정입니다.
	 * - CSRF, 인가 규칙, 폼 로그인, OAuth2 로그인, 로그아웃, 세션, 예외 처리 등을 정의합니다.
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
				/* =========================
				   CSRF 설정
				   - 일반 웹 폼은 CSRF 보호를 유지하고,
				   - REST API("/api/**")는 주로 세션이 아닌 토큰 인증을 사용하므로 예외로 둡니다.
				   ========================= */
				.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))

				/* =========================
				   인가(Authorization) 규칙
				   - 공개 페이지/로그인/회원가입/소셜 엔드포인트 등은 permitAll
				   - 나머지 모든 요청은 인증 필요(authenticated)
				   ========================= */
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/", "/login", "/signup", "/oauth2/**", "/error").permitAll()
						.anyRequest().authenticated()
				)

				/* =========================
				   폼 로그인(로컬)
				   - /login 페이지를 커스텀 로그인 페이지로 사용
				   - 로그인 성공 시 이전에 접근하던 페이지(SavedRequest)가 있으면 그리로 이동(defaultSuccessUrl("/", false))
				   - 실패 시 쿼리 파라미터로 에러 플래그 전달
				   ========================= */
				.formLogin(form -> form
						.loginPage("/login")
						.loginProcessingUrl("/login")    // POST /login (usernameParameter, passwordParameter 기준으로 인증)
						.usernameParameter("email")       // form name="email"
						.passwordParameter("password")    // form name="password"
						.defaultSuccessUrl("/", false)    // SavedRequest가 있으면 복원, 없으면 "/"로 이동
						.failureHandler((req, res, ex) -> res.sendRedirect("/login?error"))
						.permitAll()
				)

				/* =========================
				   소셜 로그인(OAuth2/OIDC)
				   - 동일한 /login 페이지에서 시작
				   - userInfoEndpoint().userService(...)로 "OAuth2UserService" 구현체를 연결
					 (여기서는 OIDC 위임 후 슬림 Principal을 반환)
				   - 성공/실패 핸들러로 후처리(예: last_login_at)나 에러 플로우 제어 가능
				   ========================= */
				.oauth2Login(oauth -> oauth
						.loginPage("/login")
						.userInfoEndpoint(u -> u.userService(socialUserService))
						.defaultSuccessUrl("/", false)
						.failureHandler((req, res, ex) -> res.sendRedirect("/login?oauth2_error"))
				)

				/* =========================
				   로그아웃
				   - /logout 호출 시 세션 종료, JSESSIONID 삭제 후 "/"로 이동
				   - 필요 시 GET 로그아웃 허용(보안상 권장 X): AntPathRequestMatcher("/logout")
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
				   - 세션 고정 보호(migrateSession)
				   - 동시 로그인 1개 제한(최근 로그인으로 이전 세션 무효화)
				   ========================= */
				.sessionManagement(sess -> sess
						.sessionFixation(sessionFixation -> sessionFixation.migrateSession())
						.maximumSessions(1)
						.maxSessionsPreventsLogin(false)
				)

				/* =========================
				   예외 처리
				   - 미인증(401): API는 JSON, 웹은 /login으로 리다이렉트
				   - 권한 없음(403): API는 JSON, 웹은 /403 페이지로
				   - 보안 관련 예외는 필터 체인에서 발생하므로 @ControllerAdvice로 잡히지 않습니다.
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
				)

				/* =========================
				   UserDetailsService 연결
				   - DaoAuthenticationProvider가 내부적으로 사용
				   - PasswordEncoder는 별도의 authenticationProvider 빈에서 주입
				   ========================= */
				.userDetailsService(localUserDetailsManager)
		;

		// 명시적으로 DaoAuthenticationProvider 등록(PasswordEncoder 확실히 연동)
		http.authenticationProvider(daoAuthenticationProvider());

		return http.build();
	}

	/**
	 * DaoAuthenticationProvider
	 * - 폼 로그인 시 UserDetailsService + PasswordEncoder 조합을 사용하여
	 * 아이디/비밀번호 검증을 수행합니다.
	 */
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider() {
		DaoAuthenticationProvider p = new DaoAuthenticationProvider();
		p.setUserDetailsService(localUserDetailsManager);
		p.setPasswordEncoder(passwordEncoder());
		return p;
	}
}