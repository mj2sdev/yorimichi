# 이름 규칙 (Naming Convention)

이 문서는 프로젝트의 코드 **가독성**과 **유지보수성을** 높이기 위한 이름 규칙을 정의합니다.
일관성 있는 코드 작성을 위해 아래 규칙을 준수해 주시기 바랍니다.

---

## 📚 목차
1. [📁 파일 이름 규칙](#📁-파일-이름-규칙)
2. [🥷🏿 커밋 메시지 규칙](#🥷🏿-커밋-메시지-규칙)
3. [ 테이블(스키마) 규칙](#테이블-스키마-규칙)
4. [코딩 이름 규칙](#코딩-이름-규칙)
---

## 📁 파일 이름 규칙
> 개발에 필요한 파일, 폴더의 이름을 정할 경우 해당 방법을 참고해 작성해주세요.  
**(카테고리) 파일명.md**

### 카테고리 목록

| category | description |
| :--- | :--- |
| note | 노트 묶음 |
| guide | 해당기능 관련 가이드 |
| list | 어떤 항목의 나열 |
| db | 데이터베이스 관련 자료 |
| back | 백엔드 관련 자료 |
| front | 프론트 관련 자료 |
| ref | 뭔가 참고자료 |
| info | 정보자료 |
| other | 무슨 카테고리인지 모를 경우 |

### 작성 예
(note) devlog  
 ㄴ (guide) intelliJ

## 🥷🏿 커밋 메시지 규칙
커밋 메시지는 기본적으로 자유롭게 작성할 수 있습니다.  
그러나 규칙이 없으면 파악에 어려움이 있을 수 있기 때문에 접두사를 붙혀 관리하고자 합니다.  

### 🪖 접두어
커밋에 접두어를 넣어 한번에 구분 가능하도록 표시하고자 합니다.  
본인이 사용할 적절한 접두사가 없는 경우 여기에 추가해 주세요.  

### ✍️ 형식
**구분(대상): 내용**   
내용은 자유롭게 쓰셔도 됩니다.  
오히려 내용 포맷을 스스로 정하여 팀원을 놀래켜보세요

### 구분
```
- add: 파일 추가
- del: 파일 삭제
- mod: 파일 수정 
- fin: 파일 완성
```
### 대상 

**common**
```
- bug 버그관련  
- test 테스트 관련
- doc 문서 관련
```

**front**
```
- img 이미지 파일
- view 화면 및 스타일
- JS 화면 기능
```

**back**
```
- feat 새로운 기능
- refactor 기능 리팩토링
- DB 데이터베이스
```

### 작성 예 1
```bash
	git commit -m "add(doc): rule_git.md 파일 추가
	- 팀원이 깃을 원활이 사용 가능하도록 문서를 추가하였습니다.
	- 자잘한 내용을 수정하였습니다."
```

### 작성 예 2
```bash
	git commit -m "del(view): trash.html 파일 삭제 (더러워서 삭제함)"
```


## 테이블-스키마-규칙

### 작성 예
제약조건
| 항목 | 규칙 |
| :--- | :--- |
| primary key | pl_[table name] |
| foreign key | fk_[current tablename]_[reference table name] |
| unique key | uk_[table name]_[column name] |
| check key | ck_[table name]_[column name] |
| not null | nn_[table name]_[column name] |

## 코딩 이름 규칙
해당 파트는 계속 업데이트되며 회의 및 논의 후 얼마든지 변경 가능합니다.
변경 시 변경 희망인원은 최소 2명 이상입니다.
한명 이상에게 설득, 논의하여 정해주시기 바랍니다.
| 구분        | 규칙 예시                         | 설명 |
|------------|----------------------------------|------|
| 클래스      | `MyClass`, `UserManager`          | **PascalCase** 사용, 명사 중심 |
| 인터페이스  | `Runnable`, `ActionListener`      | 클래스랑 동일하게 PascalCase, 대개 형용사형 + able도 많음 |
| 메소드      | `calculateTotal()`, `getName()`   | **camelCase** 사용, 동사/동사+명사 조합 |
| 변수        | `userName`, `totalAmount`         | **camelCase**, 명사형 위주 |
| 상수        | `MAX_COUNT`, `DEFAULT_VALUE`      | **대문자 + 언더스코어** (SNAKE_CASE) |
| 패키지      | `com.example.myapp`               | **소문자** + 도메인 역순, 점(.)으로 구분 |
| 열거형(Enum)| `UserStatus { ACTIVE, INACTIVE }` | 상수와 동일하게 **대문자 + 언더스코어** |
| 예외        | `FileNotFoundException`           | 클래스 이름 규칙과 동일, **Exception** 접미사 붙임 |
| 제네릭 타입 | `T`, `E`, `K`, `V`                | 한 글자 대문자 관례, T=Type, E=Element, K=Key, V=Value |
| 메소드 매개변수 | `int count`, `String fileName` | camelCase, 명확한 의미 부여 |