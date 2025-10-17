
# OAuth2 / OIDC 로그인 흐름 (다운로드 문서)

이 문서는 **컨트롤러 없이** Spring Security 필터만으로 OAuth2/OIDC 로그인을 구성하는 추천 플로우를 정리한 것입니다.  
(커스텀 로그인 페이지는 `login.html`을 사용하고, 소셜 버튼은 `/oauth2/authorization/{registrationId}` 로 보냅니다.)

---

## 1) 전체 흐름 다이어그램

```text
[사용자] 
  └─(A) GET /login  → 커스텀 로그인 페이지 (templates/user/login.html)
          └─ "Google로 로그인" 버튼: GET /oauth2/authorization/google
                    │
                    ▼
        [Provider 동의 화면] → 동의/취소 후 콜백
                    │
                    ▼
[Spring Security Filter Chain]
  └─ OAuth2LoginAuthenticationFilter (콜백 처리: /login/oauth2/code/{registrationId})
       ├─ userInfoEndpoint.userService(...)     // OAuth2 attributes
       └─ userInfoEndpoint.oidcUserService(...) // OIDC id_token/claims
          └─ SocialUserManager → (find or provision user) → Authentication 성공
                    │
                    ├─(성공) SavedRequest 복구 또는 defaultSuccessUrl("/") 리다이렉트
                    └─(실패) failureUrl("/login?oauth2_error") 리다이렉트
```

**핵심 포인트**
- 콜백(redirect_uri)와 실제 인증 처리는 **스프링 시큐리티 필터**가 담당합니다. *컨트롤러가 필요하지 않습니다.*
- 버튼은 `/oauth2/authorization/{registrationId}` 로 직접 이동시킵니다. (`google`, `naver`, `kakao` 등)

---

## 2) View (로그인 페이지)

`templates/user/login.html` – 예시(Thymeleaf 생략)

```html
<!-- 일반 폼 로그인 -->
<form action="/login" method="post">
  <input type="email" name="email" placeholder="이메일" required>
  <input type="password" name="password" placeholder="비밀번호" required>
  <button type="submit">로그인</button>
</form>

<hr>

<!-- 소셜 로그인 버튼 (필터가 처리하므로 컨트롤러 필요 없음) -->
<a href="/oauth2/authorization/google">Google로 로그인</a>
```

> `form action="/login"` 은 SecurityConfig의 `.loginProcessingUrl("/login")` 와 일치해야 합니다.

---

## 3) application.yml (클라이언트 & 콜백 설정)

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: YOUR_GOOGLE_CLIENT_ID
            client-secret: YOUR_GOOGLE_CLIENT_SECRET
            scope: openid, profile, email
            # 콜백 URL(redirect-uri) – 기본값 권장
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
```

- **콜백 URL**은 스프링이 처리하는 경로입니다. (컨트롤러 작성 불필요)
- 구글 콘솔에도 동일한 redirect URI 를 등록해야 합니다.

---

## 4) SecurityConfig 스니펫

```java
http
  .formLogin(form -> form
    .loginPage("/login")               // 커스텀 로그인 페이지 라우트
    .loginProcessingUrl("/login")      // 폼 action URL
    .usernameParameter("email")
    .passwordParameter("password")
    .defaultSuccessUrl("/", false)     // SavedRequest 우선, 없으면 홈
    .failureUrl("/login?error")        // 폼 로그인 실패
  );

http
  .oauth2Login(o -> o
    .loginPage("/login")               // 소셜 시작도 같은 페이지에서
    .userInfoEndpoint(u -> {
      u.userService(oauth2Svc);        // OAuth2
      u.oidcUserService(oidcSvc);      // OIDC
    })
    .defaultSuccessUrl("/", false)     // 소셜 성공
    .failureUrl("/login?oauth2_error") // 동의 거부/오류 시
  );
```

> **왜 컨트롤러가 필요 없나요?**  
> `/oauth2/authorization/{registrationId}` 및 `/login/oauth2/code/{registrationId}` 는 모두 **Spring Security**가 제공/처리합니다.

---

## 5) 성공/실패 시 동작

- **성공(success)**
  - 기본적으로 **SavedRequest**가 있으면 원래 요청으로 돌아갑니다.
  - 없으면 `.defaultSuccessUrl("/", false)` 에 의해 홈(`/`)으로 이동합니다.
  - 신규/기존 사용자 분기(온보딩 등)가 필요하면 **AuthenticationSuccessHandler**로 커스터마이징하세요.

- **실패(failure)**
  - `.failureUrl("/login?oauth2_error")` 로 보내고, 페이지에서 에러 메시지를 사용자에게 안내합니다.
  - 더 세밀한 처리가 필요하면 `.failureHandler(...)` 를 사용합니다.

---

## 6) SocialUserManager (개요)

1. OAuth2 / OIDC 구분하여 delegate(DefaultOAuth2UserService / OidcUserService)로 외부 사용자 정보 로드
2. `provider`, `sub(provider_user_id)`, `email` 등 추출
3. `(provider, sub)`로 내부 사용자 조회 → 없으면 **계정 생성 또는 기존 이메일 계정에 링크**
4. 최종적으로 인증 객체 생성 & SecurityContext 저장

> `(provider, provider_user_id)` 에 유니크 제약을 두어 **멱등**을 보장하세요.

---

## 7) 왜 콜백 컨트롤러를 만들지 않는가?

- 스프링 시큐리티의 **OAuth2LoginAuthenticationFilter** 가 표준 콜백 경로를 처리합니다.
- 직접 컨트롤러를 만들면 표준 흐름을 깨뜨리고 보안/호환성 이슈가 생길 수 있습니다.
- 필요한 후처리는 **Success/Failure Handler** 또는 **UserService(SocialUserManager)** 내부에서 수행하세요.

---

## 8) 체크리스트

- [ ] `/login` 뷰(templates/user/login.html) 존재, 폼 action은 `/login`
- [ ] 소셜 버튼은 `/oauth2/authorization/{registrationId}` 로 이동
- [ ] `application.yml` 에 `client-id/secret/redirect-uri` 구성
- [ ] SecurityConfig에 `.oauth2Login(...)` 및 `.formLogin(...)` 설정
- [ ] DB에 `(provider, provider_user_id)` UNIQUE 제약
- [ ] 최초 소셜 로그인 시 링크/가입 로직 정상 작동
- [ ] 실패 시 `/login?oauth2_error` 에서 사용자 안내

---

## 9) 참고 팁

- 개발 환경에서 HTTPS 없이 테스트하려면, Google OAuth의 승인된 리다이렉트 URI에 **http** 도 등록해야 합니다.  
- 도메인이 바뀌면 `{baseUrl}` 에 따라 redirect-uri도 달라집니다. 환경별 설정 분리 권장.

---

**끝.** 이 문서는 컨트롤러를 최소화하고 Spring Security의 표준 필터로 OAuth2/OIDC 로그인을 구성하는 실무형 가이드를 목표로 합니다.
