# Spring Security & OAuth2 Login Flow

<!-- @formatter:off -->

## 로컬(폼) 로그인 흐름

```less
[사용자] → POST /login (email, password)
  → [Security Filter Chain]
    → UsernamePasswordAuthenticationFilter
      → DaoAuthenticationProvider
        → LocalUserDetailsService.loadUserByUsername(email)
           └─ AccountMapper.findAuthByEmail(email)  // AccountDTO.AuthView 반환
           └─ AppUserPrincipal.fromLocal(AuthView)  // enabled/nonLocked 계산
        → PasswordEncoder(BCrypt) 매칭
      → 인증 성공 → SecurityContext 저장(AppUserPrincipal)
    → (성공 핸들러 or defaultSuccessUrl("/", false))
```

## 소셜(OIDC/OAuth2) 로그인 흐름

```less
[사용자] → GET /oauth2/authorization/google (또는 provider별 엔드포인트)  
  → [Provider 동의 화면] → 콜백
  → [Security Filter Chain]
    → OAuth2LoginAuthenticationFilter
	  → userInfoEndpoint.userService(socialUserService)
	  → SocialUserService.loadUser(OAuth2UserRequest)
	    ├─ (OIDC 요청이면) OidcUserService 위임 → OidcUser 추출
		└─ (그 외 OAuth2면) DefaultOAuth2UserService 위임 → OAuth2User 추출
		  → provider/sub/email/name/picture 파싱
		  → AccountMapper.findAuthByProviderAndSub(provider, sub)
		    └─ (없음 = 최초 로그인)
		      AccountService.signupOrLinkSocial(provider, sub, email, verified, name, picture)
              AccountMapper.findAuthByProviderAndSub(provider, sub) 재조회(정합성 보장)
		  → AppUserPrincipal.fromSocial(AuthView, provider, sub)
	  → 인증 성공 → SecurityContext 저장(AppUserPrincipal)
	→ (성공 핸들러 or defaultSuccessUrl("/", false))
```

## 최초 소셜 로그인 시 "링크/가입" 내부 흐름

```less
SocialUserService.loadUser(...)
  → (provider, sub) 조회 결과 없음
  → AccountService.signupOrLinkSocial(...)
    ├─ (동일 email 유저 존재?)
    │  └─ 있으면: 그 userId에 소셜 링크만 추가 (insertSocialAccount)
    │  └─ 없으면: root INSERT → user INSERT(소셜 전용: password NULL) → social_account INSERT 
    └─ (멱등) social_account(provider, sub) UNIQUE 로 중복 링크 방지 
  → link 후 AccountMapper.findAuthByProviderAndSub(...) 재조회
  → AppUserPrincipal 생성
```

<!-- @formatter:on -->