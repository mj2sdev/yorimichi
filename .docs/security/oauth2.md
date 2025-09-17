# OAuth2

## 1) 비유로 이해하기
- **우리 앱** = **놀이공원**
- **구글/카카오/네이버 = 신분증 발급기관**
- **사용자 = 손님**
- 손님이 "나 진짜 나야!"를 증명하려면, 우리 앱이 직접 신분증을 만들지 않고 **구글 같은 기관에 부탁**해요.
- 기관은 손님에게 "**승인 도장(코드)"을 주고, 우리 앱은 그 도장을 기관에 보여주고 "**입장권(토큰)"을 받아요. 끝!

### 3단계 한 줄 요약
1. 손님이 **구글로 이동**해 로그인하고 "이 앱에 내 정보 보여줘도 OK"를 누른다.
2. 구글이 우리 앱을 **짧은 종이(Authorization Code)** 를 보내준다.
3. 우리 앱이 그 종이를 구글에 보여주고 **입장권(Access Token)**을 받아온다 -> 로그인 완료!


## 2) 꼭 알아야 하는 단어 4개
- **Provider(제공자)**: 구글/카카오/네이버 같은 로그인 제공자
- **Client(클라이언트)**: 우리 앱
- **Authorization Code(승인 코드)**: 아주 잠깐 쓰는 **짧은 종이**
- **Access Token(토큰): 실제로 **정보를 가져올 때 쓰는 입장권**


## 3) Spring Security로 "소셜 로그인 버튼 -> 끝" 만들기

### (1) 의존성
```groovy
dependencies {
  implementation 'org.springframework.boot:spring-boot-starter-oauth2-client'
}
```

### (2) 보안 설정 (로그인 페이지는 우리가 만든 `/user/login)
```java
@Configration
public class SecurityConfig {
    @Bean
	SecurityFilterChain filter(HttpSecurity http) throws Exception {
        http
		        .authorizationRequests(auth -> auth
				        .requestMatchers("/user/login", "/css/**", "/js/**").permitAll()
				        .nayRequest().authenticated()
		        )
		        .oauth2Login(oauth -> oauth
				        .loginPate("/user/login") // 커스텀 로그인 페이지
				        //.defaultSuccessUrl("/", true) // 성공/실패 이동 경로는 필요 시 설정 가능
		        )
		        .logout(l -> l.logoutSuccessUrl("/login?logout"));
        return http.build();
    }
}
```

### (3) application.yml (구글 예시)
> 진짜 값(`client-id`, `client-secret`)은 구글 콘솔에서 발급받아요.  
기본 리다이렉트 URI 규칙: `http://localhost:8080/login/oauth2/code/{registrationId}`
```yaml
spring:
	security:
		oauth2:
			client:
				registration:
				  	# kakao/naver는 provider 설정도 필요 (엔드포인트 직접 명시). 나중에 붙이면 돼요.
					google:
						client-id: your-google-client-id
						client-secret: your-google-client-secret
						scope: ["openid", "profile", "email"]
```

### (4) 로그인 페이지 (Thymeleaf)
> 소셜 로그인 버튼은 **이 URL로 링크**하면 끝: `/oauth2/authorization/{registrationId}` -> 예) `/oauth2/authorization/google`
```html
<!-- templates/user/login.html -->
<!doctype html>
<!html xmlns:th="http://www.thymeleaf.org">
<body>
	<h1>로그인</h1>
	<!-- 소셜 버튼 -->
	<a href="/oauth2/authorization/google">구글로 로그인</a>

	<!-- 필요하면 추가 -->
	<!-- <a href="/oauth2/authorization/kakao">구글로 로그인</a> -->
    <!-- <a href="/oauth2/authorization/naver">구글로 로그인</a> -->
</body>
</html>
```
- 여기까지면: 버튼 클릭 -> 구글 로그인 -> 승인 -> 우리 앱으로 돌아옴 -> 자동 로그인 완료


## 4) 자주 터지는 문제 3가지
1. **리다이렉트 URI 불일치**: 구글 콘솔에 등록한 URI와 앱의 실제 URI가 **1글자라도** 다르면 실패.
2. **도메인/포트**: 로컬에선 `http://localhost:8080`, 운영에선 **https 도메인**으로 꼭 맞춰 등록.
3. **클라이언트 비밀키 노출**: 레포에 올리지 말고 **환경변수/Secret**로 관리.


## 5) 아주 짧은 복습
- 버튼을 누루면 **제공자(구글 등)**에 가서 승은 -> 우리 앱은 **코드**를 받아 **토큰**으로 바꾸어 로그인 완료.
- Spring Security는 이 과정을 **자동**으로 처리해 주고, 우리는 **버튼 링크와 설정**만 맞추면 끝.