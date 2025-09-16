# 이름 규칙 (Naming Convention)

이 문서는 프로젝트의 코드 가독성과 유지보수성을 높이기 위한 이름 규칙을 정의합니다.
일관성 있는 코드 작성을 위해 아래 규칙을 준수해 주시기 바랍니다.

---

## 📚 목차

1. [기본 원칙](#1-기본-원칙)
2. [용어 사전 (Glossary)](#2-용어-사전-glossary)
3. [패키지 (Package)](#3-패키지-package)
4. [클래스 및 인터페이스 (Class & Interface)](#4-클래스-및-인터페이스-class--interface)
5. [메서드 (Method)](#5-메서드-method)
6. [변수 (Variable)](#6-변수-variable)
7. [상수 (Constant)](#7-상수-constant)
8. [데이터베이스 (Database)](#8-데이터베이스-database)
9. [API 엔드포인트 (API Endpoint)](#9-api-엔드포인트-api-endpoint)

---

## 1. 기본 원칙

- **영문 사용**: 모든 이름은 영문으로 작성하는 것을 원칙으로 합니다.
- **가독성**: 의미를 명확하게 알 수 있도록 완전한 단어를 사용하고, 모호한 축약은 피합니다.
  - `좋은 예`: `calculateTotalPrice()`
  - `나쁜 예`: `calcTtlPr()`
- **일관성**: 특정 용어는 프로젝트 내에서 일관되게 사용합니다. (아래 `용어 사전` 참고)

---

## 2. 용어 사전 (Glossary)

> 프로젝트 내에서 혼용될 수 있는 단어들을 하나로 통일하여 정의합니다. **가장 중요한 규칙입니다.**

| 한글 | 영문 | 설명 |
|:---|:---|:---|
| 사용자, 회원 | `member` | `user`가 아닌 `member`로 통일합니다. |
| 게시글 | `post` | `article`, `board`가 아닌 `post`로 통일합니다. |
| 댓글 | `comment` | `reply`가 아닌 `comment`로 통일합니다. |
| 주문 | `order` | `purchase`가 아닌 `order`로 통일합니다. |
| 상품 | `product` | `item`이 아닌 `product`로 통일합니다. |

---

## 3. 패키지 (Package)

> 소문자, 도메인 역순으로 작성합니다.

```
com.yorimichi.domain.member
com.yorimichi.domain.post
com.yorimichi.global.config
```

- **구조**: `com.프로젝트명.분류.세부도메인`
  - `domain`: 핵심 비즈니스 로직 (member, post, order 등)
  - `global`: 전역적으로 사용되는 설정, 유틸 등 (config, security, util)
  - `data`: DTO, VO 등 데이터 전송 객체

---

## 4. 클래스 및 인터페이스 (Class & Interface)

> **UpperCamelCase** (PascalCase)를 사용합니다.

- **Class**: 명사 또는 명사구로 작성합니다.
  - `MemberController`, `PostService`, `MemberRepository`
- **Interface**: 클래스와 동일하게 작성하고, 구현체에 `Impl` 접미사를 붙입니다.
  - `interface MemberService` / `class MemberServiceImpl`
- **Enum**: 단수형 명사로 작성하고, `Type`, `Status` 등의 접미사를 붙여 역할을 명확히 합니다.
  - `RoleType`, `OrderStatus`

---

## 5. 메서드 (Method)

> **lowerCamelCase**를 사용하며, `동사 + 명사` 형태로 작성합니다.

- **조회 (Read)**: `get`, `find`
  - `getMemberById(Long id)` / `findAllPosts()`
- **생성 (Create)**: `create`, `save`, `add`
  - `createPost(PostRequestDto dto)`
- **수정 (Update)**: `update`
  - `updateMemberInfo(MemberRequestDto dto)`
- **삭제 (Delete)**: `delete`, `remove`
  - `deletePostById(Long id)`
- **검증/확인**: `is`, `has`, `exists` (주로 boolean 반환)
  - `isPasswordCorrect()` / `existsByEmail(String email)`

---

## 6. 변수 (Variable)

> **lowerCamelCase**를 사용하며, 명사로 작성합니다.

- **일반 변수**: `memberName`, `totalPrice`
- **Boolean 변수**: `is` 또는 `has`로 시작하여 질문 형태로 작성합니다.
  - `boolean isVisible = true;` / `boolean hasPermission = false;`

---

## 7. 상수 (Constant)

> **SNAKE_UPPER_CASE**를 사용합니다.

```java
public static final int MAX_LOGIN_ATTEMPTS = 5;
public static final String DEFAULT_ROLE = "ROLE_MEMBER";
```

---

## 8. 데이터베이스 (Database)

> **snake_lower_case**를 사용합니다.

- **테이블**: 복수형 명사로 작성합니다.
  - `members`, `posts`, `comments`
- **컬럼**: 단수형 명사 또는 `테이블명_컬럼명` 형태로 작성합니다.
  - `member_name`, `created_at`
- **기본 키 (PK)**: `id` 또는 `테이블단수형_id` (예: `member_id`)
- **외래 키 (FK)**: `참조하는테이블단수형_id` (예: `member_id`)

---

## 9. API 엔드포인트 (API Endpoint)

> 소문자, 복수형 명사를 사용하며, 동사 대신 HTTP Method로 행위를 표현합니다.

- **URL**: `/자원(복수형)/식별자`
  - `GET /members` (모든 회원 조회)
  - `GET /members/{memberId}` (특정 회원 조회)
  - `POST /members` (회원 생성)
  - `PUT /members/{memberId}` (회원 정보 전체 수정)
  - `PATCH /members/{memberId}` (회원 정보 일부 수정)
  - `DELETE /members/{memberId}` (회원 삭제)

