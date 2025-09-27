# 스프링 Validator

> 이 문서는 **Spring Boot 3 + Jakarta Bean Validation** 기준입니다.  
> “데이터가 제대로 왔는지 검사하는 안전벨트”라고 생각하면 됩니다.

---

## 1. 왜 필요한가? (왜 안전벨트가 필요하지?)

- 우리가 받는 값(예: `id`, `name`)이 **비어 있거나**, **너무 짧거나**, **숫자가 아닌데 숫자여야** 할 수 있어요.
- 이런 **잘못된 값**을 초기에 막아 주는 것이 바로 **Validator(검사기)** 입니다.
- 검사를 통과하지 못하면 **친절한 에러(400 Bad Request)** 를 내보내요.

---

## 2. 설치(의존성) — 준비물 붙이기

**Gradle**

```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-validation'
}
```

> 이 한 줄에 **Hibernate Validator**(검사 엔진)와 **Jakarta Validation**(규칙)이 함께 들어옵니다.

---

## 3. 어디에 붙이나? (검사는 어디서 하죠?)

- **DTO(데이터 통)**: “규칙”을 적는 곳 (예: 이름은 비면 안 돼!)
- **Controller(경계)**: 실제로 검사를 “실행”하는 곳 (예: 손님이 보낸 값 검사)
- **Service(심화)**: 배치/메시지 등 **웹 말고 다른 경로**로도 들어올 때 **추가 안전망**

그림으로 보면:

```
손님 → Controller(@Valid) → Service(@Validated) → Mapper/DB
                 ↑ DTO에 규칙(@NotBlank, @Min 등)
```

---

## 4. 가장 많이 쓰는 어노테이션(스티커)

- `@NotNull` : 값이 **null이면 안 돼!**
- `@NotBlank` : **문자열은 비면 안 돼!** (`"   "`도 안 됨)
- `@Size(min=, max=)` : 글자 수/개수 제한
- `@Min`, `@Max`, `@Positive` : 숫자 범위 검사
- `@Email` : 이메일 모양 검사
- `@Pattern(regexp="...")` : 정규식 패턴(특정 모양) 검사
- `@Valid` : **안에 있는 것들도 같이 검사해줘!**(연쇄 검사)

> **중요!** `@Min`은 **`jakarta.validation.constraints.Min`** 를 임포트하세요.  
> (Checker Framework의 `@MinLen` 같은 건 **아니에요**.)

---

## 5. DTO에 규칙 붙이기 (규칙표 만들기)

```java
import jakarta.validation.constraints.*;

public class StoreDTO {
	@NotNull(groups = Update.class) // 수정 때는 반드시 있어야 함
	private Long id;

	@NotBlank // 빈 문자열 금지
	@Size(max = 50)
	private String name;

	@Valid                    // 안에 또 DTO가 있으면 함께 검사
	private AddressDTO address;
}
```

```java
public class AddressDTO {
	@NotBlank
	private String city;
	@NotBlank
	private String street;
	@NotBlank
	private String zip;
}
```

> **그룹(groups)**: 상황별 규칙 나누기. 예) 생성(Create)과 수정(Update)이 다를 때

---

## 6. Controller에서 실행하기 (진짜 검사 버튼 누르기)

```java

@RestController
@Validated // 단일 값(스칼라)도 검사하려면 있으면 좋아요
@RequestMapping("/stores")
public class StoreController {
	private final StoreService storeService;

	public StoreController(StoreService storeService) {
		this.storeService = storeService;
	}

	@GetMapping("/{id}")
	public StoreDTO get(@PathVariable @Min(1) long id) {
		return storeService.findById(id);
	}

	@PostMapping
	public long create(@RequestBody @Valid StoreDTO dto) {
		return storeService.save(dto);
	}
}
```

- `@Valid StoreDTO dto` → DTO 규칙 실행
- `@Min(1) long id` → “숫자는 1 이상!” 같은 **단일 값(스칼라) 검사**

---

## 7. Service에서도 가드하기 (추가 안전망)

```java

@Service
@Validated // public 메서드 파라미터/리턴 검증 활성화
public class StoreManager implements StoreService {

	@Override
	public StoreDTO findById(@Min(1) long storeId) {
		// ... 조회
	}

	@Override
	public long save(@Valid StoreDTO dto) {
		// 웹 말고도 배치/메시지에서 호출할 수 있으면 @Valid 유지
		// 오직 컨트롤러만 진입점이면 생략해도 OK
		// ... 저장
	}
}
```

> 프록시 기반이라서 **public 메서드**에서 동작합니다. 자기 자신 호출(내부 호출)에는 적용되지 않아요.

---

## 8. 에러는 어떻게 나오나요? (전역 처리기)

스프링은 기본적으로 400을 내보내지만, **더 예쁘게** 만들려면 `@ControllerAdvice` 사용:

```java

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class) // DTO 바인딩 실패
	public ResponseEntity<?> handleMethodArgNotValid(MethodArgumentNotValidException ex) {
		// 필드별 메시지 모아서 400 응답
		return ResponseEntity.badRequest().body(...);
	}

	@ExceptionHandler(ConstraintViolationException.class) // 스칼라 검사 실패
	public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {
		return ResponseEntity.badRequest().body(...);
	}
}
```

---

## 9. 자주 하는 실수 (주의!)

- `@Min` 대신 **다른 패키지**(Checker Framework 등)를 임포트함 → **반드시 `jakarta.validation...`**
- DTO에 규칙은 있는데 **Controller에서 @Valid를 안 붙임** → 검사가 실행되지 않음
- `Long`을 쓰면서 **@NotNull을 빼먹음** → null 들어와도 통과
- Service에 `@Validated`를 안 붙이고 **스칼라 제약**만 붙임 → 실행 안 됨
- **그룹(groups)** 을 안 쓰고 생성/수정 규칙을 한 DTO에 섞어 혼란

---

## 10. 미니 체크리스트

- [ ] `spring-boot-starter-validation` 추가했나요?
- [ ] DTO 필드에 규칙(@NotBlank, @Size, @Min …) 붙였나요?
- [ ] Controller 메서드에 `@Valid`(DTO), `@Min/@NotBlank`(단일 값) 붙였나요?
- [ ] Service에 `@Validated` 붙였나요? (추가 안전망 필요 시)
- [ ] 전역 예외 처리로 400 응답을 예쁘게 만들었나요?
- [ ] 생성/수정을 그룹으로 나눴나요? (필요 시)

---

## 11. 예제 한 방 정리

```java
// DTO
public class SearchDTO {
	@Positive
	int page = 1;
	@Positive
	int size = 10;
	@Size(max = 50)
	String keyword;
}

// Controller
@GetMapping("/search")
public PageResponse<StoreDTO> search(@Valid SearchDTO dto) {
	return storeService.findAll(dto);
}

// Service
@Service
@Validated
public class StoreManager implements StoreService {
	@Override
	@Transactional(readOnly = true)
	public PageResponse<StoreDTO> findAll(@Valid SearchDTO dto) { ...}

	@Override
	@Transactional(readOnly = true)
	public StoreDTO findById(@Min(1) long id) { ...}
}
```

---

## 12. 한 줄 요약

- **규칙은 DTO에**, **실행은 Controller에서**, **Service는 추가 안전망**.
- 올바른 패키지(`jakarta.validation.*`)로 임포트만 잘 하면, **Validator는 든든한 안전벨트**가 됩니다!

---

## 부록 A. 지금까지 나온 어노테이션 한눈에 보기

> *어떤 패키지에서 가져와야 하는지, 어디서 동작하는지, 실패하면 어떤 예외가 나는지*를 요약했어요.

| 어노테이션                          | 올바른 import (패키지)                                       | 역할/규칙                                | 보통 붙이는 곳                                | 실패 시 대표 예외                                                                     |
|--------------------------------|--------------------------------------------------------|--------------------------------------|-----------------------------------------|--------------------------------------------------------------------------------|
| `@Valid`                       | `jakarta.validation.Valid`                             | **객체 내부까지 연쇄 검증**(중첩 DTO/컬렉션) 실행 트리거 | 컨트롤러/서비스 메서드 파라미터, DTO 필드               | (컨트롤러) `MethodArgumentNotValidException`, (서비스) `ConstraintViolationException` |
| `@Validated`                   | `org.springframework.validation.annotation.Validated`  | **스프링 AOP 기반 메서드 검증 활성화**, 그룹 지정 가능  | 컨트롤러 클래스, 서비스 클래스                       | (서비스 스칼라 검증) `ConstraintViolationException`                                    |
| `@NotNull`                     | `jakarta.validation.constraints.NotNull`               | `null` 금지                            | DTO 필드, 서비스 파라미터(래퍼 타입)                 | 위와 동일                                                                          |
| `@NotBlank`                    | `jakarta.validation.constraints.NotBlank`              | 공백 포함 비어있는 **문자열** 금지                | DTO 문자열 필드                              | `MethodArgumentNotValidException` 등                                            |
| `@NotEmpty`                    | `jakarta.validation.constraints.NotEmpty`              | 비어있는 값 금지(문자열/컬렉션)                   | DTO 필드                                  | 동일                                                                             |
| `@Size(min, max)`              | `jakarta.validation.constraints.Size`                  | 길이/개수 범위 제한                          | 문자열/컬렉션/배열                              | 동일                                                                             |
| `@Min`, `@Max`                 | `jakarta.validation.constraints.Min/Max`               | 정수 최소/최대                             | id, 카운트 등 정수 값                          | 동일                                                                             |
| `@Positive`, `@PositiveOrZero` | `jakarta.validation.constraints.Positive*`             | 양수/0 이상                              | 각종 id, 페이지/사이즈                          | 동일                                                                             |
| `@DecimalMin`, `@DecimalMax`   | `jakarta.validation.constraints.DecimalMin/DecimalMax` | 실수 최소/최대                             | 위도/경도, 금액 등 실수                          | 동일                                                                             |
| `@Digits(integer, fraction)`   | `jakarta.validation.constraints.Digits`                | 정수부/소수부 자릿수 제한                       | 좌표 DECIMAL 정밀도 등                        | 동일                                                                             |
| `@Pattern(regexp)`             | `jakarta.validation.constraints.Pattern`               | 정규식 패턴 일치                            | 전화번호, 상태값 문자열                           | 동일                                                                             |
| `@Email`                       | `jakarta.validation.constraints.Email`                 | 이메일 형식 체크                            | `user.email` 등                          | 동일                                                                             |
| `@URL`                         | `org.hibernate.validator.constraints.URL`              | URL 형식 체크(Hibernate Validator 확장)    | 이미지/사이트 링크                              | 동일                                                                             |
| `@Past`, `@PastOrPresent`      | `jakarta.validation.constraints.Past*`                 | 과거/현재까지 허용                           | `createdAt`, `updatedAt`, `lastLoginAt` | 동일                                                                             |
| `@Future`, `@FutureOrPresent`  | `jakarta.validation.constraints.Future*`               | 미래/현재 이후 허용                          | `meetingAt` 같은 예약 시간                    | 동일                                                                             |
| `@Null`                        | `jakarta.validation.constraints.Null`                  | **반드시 null이어야 함**                    | 외부가 입력하면 안 되는 서버 관리 필드                  | 동일                                                                             |

### 사용 팁

- **반드시 올바른 패키지**에서 import: `jakarta.validation.*` (Spring Boot 3 기준).
	- 예) `@Min` → `jakarta.validation.constraints.Min` (❌ `org.checkerframework...` 아님)
- **컨트롤러**에서 DTO는 `@Valid`, Path/Query의 **단일 값(스칼라)** 은 `@Min`, `@NotBlank` 같이 직접 제약을 붙입니다.
- **서비스**에 `@Validated`를 붙여야 **메서드 파라미터 검증**이 동작합니다(프록시 기반, public 메서드).

---

## 부록 B. 그룹(groups) 예시: Create vs Update

```java
public interface Create {
}

public interface Update {
}

public class RootDTO {
	@Null(groups = Create.class)
	@NotNull(groups = Update.class)
	private Long id;

	@PastOrPresent
	@Null(groups = Create.class)
	private LocalDateTime createdAt;
}
```

컨트롤러에서 실행:

```java

@PostMapping("/stores")
public long create(@RequestBody @Validated(Create.class) StoreDTO dto) { ...}

@PutMapping("/stores/{id}")
public void update(@RequestBody @Validated(Update.class) StoreDTO dto) { ...}
```

---

## 부록 C. 스키마 매핑 예시(좌표, ENUM, 전화)

```java
// 좌표: DECIMAL(9,6)/(10,6)
@DecimalMin("-90.0")
@DecimalMax("90.0")
@Digits(integer = 3, fraction = 6)
private BigDecimal latitude;

@DecimalMin("-180.0")
@DecimalMax("180.0")
@Digits(integer = 3, fraction = 6)
private BigDecimal longitude;

// ENUM: 간단히 Pattern으로
@Pattern(regexp = "OPEN|CLOSED|CANCELLED")
private String status;

// 전화번호(국제포맷 범용 예)
@Pattern(regexp = "^[+0-9][0-9\- ]{6,19}$")
@Size(max = 20)
private String phone;
```

> 업데이트 시간: 2025-09-26 06:25:39