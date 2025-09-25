# Spring Security Login

## 한 줄 그림

### **문지기(Security)** 에게

1. **어떤 문은 누구나 통과**(로그인 페이지 등),
2. **어떤 문은 관리자만**,
3. 나머지는 **로그인한 사람만** 이라고 규칙을 알려주고,
4. **로그인 창 위치와 성공/실패 시 어디로 갈지**를 알려주는 거야.

### 규칙(길/문) 정하기 - `authorizeHttpRequests(...)`

- `"/user/login", "/css/**" ... -> permitAll()`: **학교 안내문** 같은 곳, 누구나 볼 수 있어.
- `"/admin/**", -> hasRole("ADMIN")`: 교무실 문. **관리자**만 들어가요.
- `anyRequest().authenricated()`: 그 밖의 문을 **학생증(로그인)** 있어야 통과!

### 로그인 창/처리 - `formLogin(...)`

- `loginPage("/user/login)`: "**로그인 창은 여기**에요." (GET /user/login 화면)
- `loginProcessingUrl("/user/login")"`: **"로그인 제출은 여기**로 와요." (POST /user/login)
- `usernameParameter("email")`, `passwordParameter("password")`: "아이디 칸 이름은 **email**, 비번 칸 이름은 **password**야." (HTML
  `<input name="email">`와 꼭 같아야 함)
- `defaultSuccessUrl("/", true)`: "성공하면 **홈(/)** 으로 가요."
- `failureUrl("/user/login?error")`: "실패하면 **다시 로그인 페이지**로, 에러 표시해요."

### 로그아웃 - `logout(...)`

- `logoutUrl("/user/logout")`: "**여기로 나오면**(로그아웃) 돼요."
- `logoutSuccessUrl("/user/login?logout")`: "나오면 **로그아웃 완료** 문구 보여줘요."

### CSRF(보안 스티커)

- 폼 로그인에선 **CSRF 보호가 기본 ON**.
- 템플릿 폼에 **숨은 스티커**를 붙여야 해요.

```html
<input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}">
```

- 이게 있으면 "정말 우리 학교에서 낸 폼 맞다"는 표시가 돼요.

### 전부 합치면 무슨 일?

1. 사용자가 **/user/login**에 가면 내가 만든 로그인 화면이 보임.
2. 폼을 **POST /user/login**으로 보내면 문지기가 아이디/비번 확인.
3. 성공하면 / 로 입장, 실패하면 **/user/login?error**로 돌아가 안내.
4. **/admin/** 은 관리자만, 그 외 페이지는 **로그인해야** 볼 수 있음.