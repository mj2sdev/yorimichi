<!-- @formatter:off -->

# 인증·회원관리 아키텍처 개요 (Spring Security + MyBatis)

> 이 문서는 현재 구현된 **Spring Security, User/Account 도메인, 프린시펄, 서비스/매퍼 레이어** 전반의 흐름을 팀원이 빠르게 이해할 수 있도록 정리한 **요약 가이드**입니다.

---

## 구성요소 한눈에 보기

- **SecurityConfig**
	- 폼 로그인(로컬) + OAuth2/OIDC(소셜) 동시 구성
	- `LocalUserDetailsManager`를 `UserDetailsService`로 사용
	- 소셜은 `SocialUserManager`를 **OAuth2**/**OIDC** 각각의 `OAuth2UserService`로 연결
	- 세션 고정 보호, 동시 세션 1개 제한, 미인증/권한없음 응답 포맷 등 공통 정책


- **RootDTO / UserDTO / SocialAccountDTO**
	- `RootDTO`: 공통 루트(soft delete, blind 상태 포함)
	- `UserDTO`: 인증/프로필 최소 필드 (`role`, `email`, `password(hash)`, `nickname`, `lastLoginAt` 등)
	- `SocialAccountDTO`: 소셜 링크 키와 프로필 (`provider`, `providerUserId`, `providerEmail`, `emailVerified`, `displayName`, `avatarUrl`)


- **AppUserPrincipal**
	- 세션에 싣는 **슬림 프린시펄**
	- `UserDetails` + `OAuth2User` 구현
	- 팩토리: `fromLocal(UserDTO)`, `fromSocial(UserDTO, SocialAccountDTO)`
	- 필수 필드: `userId`, `email`, `authorities`, `enabled`, `nonLocked` (+ 로컬만 `password`, 소셜이면 `provider`, `providerUserId`)


- **LocalUserDetailsManager**
	- 폼 로그인 시 호출
	- `AccountMapper.selectByEmail(email)`로 최소 정보 조회 → `AppUserPrincipal.fromLocal(...)` 변환
	- 이메일 정규화(Trim/NFKC/Lower)는 서비스에서 보장


- **SocialUserManager**
	- 소셜 로그인 시 **외부 프로필 로드 → 내부 계정 매핑/신규 생성**
	- 진입점 2개:
		- `loadOAuth2User(OAuth2UserRequest)` (GitHub/Naver/Kakao 등)
		- `loadOidcUser(OidcUserRequest)` (Google 등)
	- 키 추출: `provider`, `providerUserId(sub)`, `email`, `name`, `picture`, `emailVerified`
	- `(provider, providerUserId)`로 기존 유저 조회 → 없으면 `AccountService.signupOrLinkSocial(...)` 호출
	- OIDC 경로는 `DefaultOidcUser`, OAuth2 경로는 `AppUserPrincipal` 반환


- **AccountService / AccountManager**
	- 회원가입/소셜 링크/비밀번호 변경/탈퇴(soft delete) 등 **계정 도메인 로직**
	- `signupLocal(UserDTO)`: 이메일 정규화·중복검사 → `root` INSERT → `user` INSERT(BCrypt)
	- `signupOrLinkSocial(SocialAccountDTO)`:  
	  ① `(provider, sub)`로 조회 → 있으면 로그인 갱신  
	  ② 없으면 `providerEmail`로 기존 유저 매칭 → 있으면 **소셜만 링크**  
	  ③ 그래도 없으면 **신규 root→user→social_account** 생성  
	  이후 `lastLoginAt` 갱신
	- `changePassword(...)`: BCrypt로 변경
	- `deleteAccount(...)`: soft delete 권장(`root.deleted_at` 세팅)


- **AccountMapper (인터페이스)**
	- 읽기: `selectByEmail`, `selectUserByProviderAndSub`, `existsNickname`
	- 쓰기: `insertUser`, `insertSocialAccount`, `updateLastLoginAt`, `updateSocialLastLoginAt`, `updatePassword`, `deleteUser(soft)`


- **AccountMapper.xml (요지)**
	- `map-underscore-to-camel-case: true`, `type-aliases-package`로 간결화
	- 인증용 SELECT는 **삭제/블라인드 조건을 미리 걸지 않음** → 서비스에서 `enabled/nonLocked` 계산
	- 닉네임 중복은 정책에 따라 공용 `<sql>` 조각(`RootNotDeleted` 등) 포함 가능
	- `insertUser`의 `role_id`는 `(SELECT id FROM role WHERE name='USER')`로 lookup

---

## 인증 흐름(시나리오)

### A) 로컬 로그인(폼)

```text
[User] -- 이메일/비밀번호 --> [/login]
   └──> DaoAuthenticationProvider
         └──> LocalUserDetailsManager.loadUserByUsername(email)
               └──> AccountMapper.selectByEmail(email)
                     └──> UserDTO → AppUserPrincipal.fromLocal(...)
                           └──> SecurityContext 인증 성공(세션)
```

- 비밀번호 해시는 `DaoAuthenticationProvider`가 검증
- 로그인 시각 갱신은 후처리(컨트롤러/필터/리스너 등)로 필요 시 추가

---

### B) 소셜 로그인(OAuth2/OIDC)

```text
[User] -- 소셜 로그인 버튼 -->
  Provider 인증/콜백
    └──> Spring Security (oauth2Login)
          ├──> SocialUserManager.loadOidcUser (OIDC인 경우)
          │     └── OidcUserService로 claims 로드(sub/email/verified/...)
          └──> SocialUserManager.loadOAuth2User (OAuth2인 경우)
                └── DefaultOAuth2UserService로 attributes 로드(id/email/...)

[SocialUserManager]
  ├─ 키 추출: provider, providerUserId(sub), email, name, picture, verified
  ├─ AccountMapper.selectUserByProviderAndSub(...)
  │    ├─ 있으면: 로그인 시각 갱신 → Principal 반환
  │    └─ 없으면: AccountService.signupOrLinkSocial(...) 호출
  │         ├─ providerEmail로 기존 유저 매칭 → 있으면 소셜만 링크
  │         └─ 없으면 신규 root→user(소셜: password=null)→social_account 생성
  └─ 최종 Principal 반환
       ├─ OIDC: DefaultOidcUser
       └─ OAuth2: AppUserPrincipal
```

---

### C) 로컬 회원가입

```text
Controller → AccountService.signupLocal(UserDTO)
   ├─ 이메일 정규화/중복검사
   ├─ root INSERT (user.id = root.id)
   ├─ password BCrypt 해시
   └─ AccountMapper.insertUser(user)  // role_id = (SELECT id FROM role WHERE name='USER')
```

---

### D) 닉네임 중복 검사

- `AccountMapper.existsNickname(nickname) : boolean`
- 정책 예시:
	- **삭제된 계정은 닉네임 반납** → 공용 `<sql>` `RootNotDeleted`만 걸어 검사
	- **블라인드 계정도 닉네임 유지** → 삭제만 체크

```xml
<!-- 예시 -->
<select id="existsNickname" resultType="boolean">
  SELECT EXISTS(
    SELECT 1
    FROM user u
    JOIN root r ON r.id = u.id
    WHERE u.nickname = #{nickname}
      AND r.deleted_at IS NULL   <!-- RootNotDeleted -->
    LIMIT 1
  )
</select>
```

---

### E) 회원 탈퇴(권장: Soft Delete)

- `AccountService.deleteAccount(userId)` → `AccountMapper.deleteUser(userId)`
- 내부 구현은 `UPDATE root SET deleted_at = NOW() WHERE id = ? AND deleted_at IS NULL`
- **이메일 재사용**은 보안상 금지(일반적 권고). 닉네임은 정책에 따라 반납/유지 결정

---

## 설계상의 핵심 원칙

1. **슬림 프린시펄**  
   세션/직렬화 부하를 줄이기 위해 인증·인가에 필요한 최소 필드만 `AppUserPrincipal`에 보관. (토큰/클레임 덩어리는 X)

2. **상태 플래그 파생**  
   `RootDTO.deletedAt / blindedAt` → `enabled` / `nonLocked` 파생.  
   인증용 SELECT에서 **미리 거르지 않고** 상태 값을 읽어 서비스에서 계산.

3. **소셜 절대 키**  
   `(provider, providerUserId)`는 **절대 키**.  
   최초 로그인 시 `providerEmail`이 있으면 기존 유저에 **링크**하고, 아니면 새로 **프로비저닝**.

4. **입력 정규화**  
   이메일/닉네임은 **trim + NFKC + lower** 등 **정규화 일관성** 유지.

5. **DB 시간 사용**  
   이력/로그 갱신은 `NOW()`/`CURRENT_TIMESTAMP`로 DB 시간 사용.  
   DDL 기본값/자동갱신은 `CURRENT_TIMESTAMP` 사용.

6. **MyBatis 설정**
	- `mapper-locations: classpath*:mappers/**/*.xml`
	- `type-aliases-package: com.jslhrd.yorimichi.domain`
	- `configuration.map-underscore-to-camel-case: true`
	- XML `<mapper namespace="...">`는 **인터페이스 FQCN**과 정확히 일치

---

## 자주 하는 실수 체크리스트

- [ ] 소셜 신규가입 시 `insertSocialAccount` 전에 **`socialAccount.userId` 세팅**했는가?
- [ ] 인증용 SELECT에서 `RootVisible` 같은 조건으로 **유저를 미리 걸러버리지 않았는가?**
- [ ] 이메일/닉네임 정규화 로직이 **가입·로그인·중복검사** 전 과정에서 **일관**적인가?
- [ ] `role_id`를 숫자 하드코딩하지 않고 `(SELECT id FROM role WHERE name='USER')`로 lookup 하는가?
- [ ] `mapper-locations` 접두사 `classpath*:`를 썼는가?
- [ ] 인증 실패 메시지를 **일반화**(유저 열거 방지)했는가? (`Bad credentials`)

---

## 부록: 설정 스니펫

**application.yml**
```yaml
mybatis:
  configuration:
    map-underscore-to-camel-case: true
  mapper-locations: classpath*:mappers/**/*.xml
  type-aliases-package: com.jslhrd.yorimichi.domain
```

**AccountMapper.xml (요지)**
```xml
<mapper namespace="com.jslhrd.yorimichi.mapper.AccountMapper">
  <select id="selectByEmail" parameterType="string" resultType="UserDTO">
    SELECT r.id, r.type, r.created_at, r.updated_at, r.deleted_at, r.blinded_at,
           u.email, u.password, u.nickname, u.description, u.last_login_at,
           ro.name AS role
    FROM user u
    JOIN root r ON r.id = u.id
    JOIN role ro ON ro.id = u.role_id
    WHERE u.email = #{email}
    LIMIT 1
  </select>

  <select id="selectUserByProviderAndSub" resultType="UserDTO">
    SELECT r.id, r.type, r.created_at, r.updated_at, r.deleted_at, r.blinded_at,
           u.email, u.password, u.nickname, u.description, u.last_login_at,
           ro.name AS role
    FROM social_account sa
    JOIN user u  ON u.id = sa.user_id
    JOIN root r  ON r.id = u.id
    JOIN role ro ON ro.id = u.role_id
    WHERE sa.provider = #{provider}
      AND sa.provider_user_id = #{providerUserId}
    LIMIT 1
  </select>

  <insert id="insertUser" parameterType="UserDTO">
    INSERT INTO user (id, role_id, email, password, nickname, description)
    VALUES (#{id},
            (SELECT id FROM role WHERE name='USER'),
            #{email}, #{password}, #{nickname}, #{description})
  </insert>

  <insert id="insertSocialAccount" parameterType="SocialAccountDTO">
    INSERT INTO social_account (
      user_id, provider, provider_user_id, provider_email, email_verified, display_name, avatar_url
    ) VALUES (#{userId}, #{provider}, #{providerUserId}, #{providerEmail},
              #{emailVerified}, #{displayName}, #{avatarUrl})
  </insert>

  <update id="updateLastLoginAt">
    UPDATE user SET last_login_at = NOW() WHERE id = #{userId}
  </update>

  <update id="updateSocialLastLoginAt">
    UPDATE social_account SET last_login_at = NOW()
    WHERE user_id = #{userId} AND provider = #{provider}
  </update>

  <update id="updatePassword">
    UPDATE user SET password = #{hash} WHERE id = #{userId}
  </update>

  <update id="deleteUser">
    UPDATE root SET deleted_at = NOW()
    WHERE id = #{userId} AND deleted_at IS NULL
  </update>
</mapper>
```

<!-- @formatter:on -->