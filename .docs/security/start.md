# Spring Boot 3.x + Spring Security 6 예시

## 1) 비유로 이해하기

- 보안문지기(Security) = 학교 정문 경비 아저씨
- 이름표(아이디/비밀번호) = 누구인지 증명하는 것(로그인)
- 출입 규칙 = “여긴 누구나 OK, 여긴 선생님만” 같은 규칙
- 문지기한테 알려줄 것
- 누가 들어올 수 있는지(계정)
- 어떤 길(URI)은 누가 지나갈 수 있는지(권한 규칙)
- Spring Security는 기본적으로 모든 문은 닫혀 있고, 로그인한 사람만 통과하게 만들어져 있어. 거기서 우리가 “이 길은 모두 통과 가능”, “여긴 ADMIN만” 같은 규칙을 덧붙이면 돼.

### 폴더 구조

```swift
src/main/java
└─ com/example/myapp
   ├─ MyAppApplication.java        // @SpringBootApplication
   ├─ config
   │  └─ SecurityConfig.java       // 여기!
   └─ web
      └─ HelloController.java
```

#### 왜 여기?

- Spring Boot는 `@SpingBootApplication`이 선언된 클래스의 **패키지부터 하위 패키지들을 자동 스캔**합니다.
  그래서 `SecurityConfig`를 그 **하위 패키지(예:`config`)**에 두면 자동으로 읽혀요.

## 2) 바로 되는 최소 예제

### (1) 의존성 추가

**build.gradle**

```gradle
dependencies {
  implementation 'org.springframework.boot:spring-boot-starter-web'
  implementation 'org.springframework.boot:spring-boot-starter-security'
}
```

### (2) 계정 & 보안 규칙 설정

**SecurityConfig.java**

- 위 설정 방식은 SecurityFilterChain 빈으로 규칙을 선언하는 현재 권장 방식.

```java
import java.beans.BeanProperty;

@Configuration
public class SecurityConfig {

    // 1) 비밀번호 암호화하기
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    // 2) 메모리 계정 2개 (USER, ADMIN)
    public UserDetailService users(PasswordEncoder encoder) {
        
        var user = User.withUsername("user")
	        .password(encoder.encoder("1234"))
	        .roles("USER") // ROLE_USER
	        .build();
        var admin = User.withUsername("admin")
            .password(encoder.encoder("1234"))
            .roles("ADMIN") // ROLE_ADMIN
            .build();
        
        return new InMemoryUserDetailsManager(user, admin);
    }
    
    // 3) 어떤 길(URI)에 누가 들어올 수 있는지 규칙
    @Bean

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        http
            .authrizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll() // 모두 통과
		            .requestMatchers("/admin/**").hasRole("ADMIN") // ADMIN만 통과
		            .anyRequest().authenticated() // 그 외엔 로그인 필요
            )
		        .formLogin(Customizer.withDefaults()) // 기본 로그인 페이지 제공
		        .httpBasic(Customizer.withDefaults()); // curl 등으로 테스트할 때 편함
		
		return http.build();
    }
}
```

### (3) 컨트롤러(길 만들기)

**HelloController.java**

```java
@RestController
public class HelloController {
	
    @GetMapping("/public/hello") {
    	return "누구나 볼 수 있어요.";    
    }
    
    @GetMapping("/me")
	public String me() {
        return "로그인한 사람만 볼 수 있어요.";
    }
    
    @GetMapping("/admin/secret")
	public String admin() {
        return "관리자만 볼 수 있어요.";
    }
}
```

### (4) 이렇게 시험

```bash
# 1) 모두 접근 가능
curl http://localhost:8080/public/hello

# 2) 로그인 필요 (미로그인 시 401 또는 로그인 페이지로 리다이렉트)
curl http://localhost:8080/me

# 3) 사용자로 로그인 (HTTP Basic)
curl -u user:1234 http://localhost:8080/me

# 4) 관리자 전용
curl -u admin:1234 http://localhost:8080/admin/secret
```

## 3) 자주 묻는 것들

### Q1) 왜 비밀번호를 꼭 암호화해?

- 비밀번호는 평문으로 저장하면 절돼 안 돼. 위에 쓴 `BCryptPasswordEncoder`가 표준이야.

### Q2) CSRF는?

- **브라우저 폼**을 쓰는 앱이면 Spring Security가 **기본으로 CSRF 보호**를 켜줘.
  JSON API만 쓰고 **완전한 REST/JWT** 구조라면 보통 **세션을 끄고(CSRF도 끄는 편) 설계해.
  (무턱대고 끄지 말고, 아키텍처에 맞춰 결정!)

### Q4) "ROLE_" 접두사는 뭐야?

- 코드에선 `hasRole("ADMIN")`처럼 쓰지만, 내부에선 자동으로 `ROLE_ADMIN` 권한으로 매칭돼.

## 4) (보너스) 완전 API 모드의 기본 뼈대

- SPA + 백엔드 분리 환경에서 자주 쓰는 기본기 (세션 끄기, CSRF 비활성화).
- 이 부분은 설계에 따라 달라지미, need가 생기면 JWT/Resource Server 설정을 이어서 붙이면 돼. (여기선 핵심 흐름만)

```java
import java.beans.BeanProperty;

@Bean
public SecurityFilterChain apiChain(HttpSecurity http) throws Exception {
	
    http
	    .csrf(csrf -> csrf.disable()) // JWT 기반 등 비상태 API라면 비활성화 고려
	    .authorizeHttpRequests(auth -> auth
		    .requestMatchers("/public/**").permitAll()
		    .anyRequest().authenticated()
	    )
	    .sessionManagerment(sm -> sm.sessionCreationPolicy(
                org.springframeword.security.config.http.SessionCreationPolicy.STARELESS
	    ));
    
    // 여기에 JWT 필터/리소스 서버 설정을 추가하게 됨
	return http.build();
}
```

## 한 줄 정리

1. 계정을 만들고(사용자/권한)
2. 길(URI)마다 규칙 정하고
3. 문지기(SecurityFilterChain)에 알려주면 끝!
	- 기본은 "모두 잠금 -> 우리가 여는 곳만 열기" 라는 점만 기억해 둬.