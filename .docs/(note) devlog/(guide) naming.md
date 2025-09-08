# 이름 규칙 (Naming Convention)

이 문서는 프로젝트의 코드 **가독성**과 **유지보수성을** 높이기 위한 이름 규칙을 정의합니다.
일관성 있는 코드 작성을 위해 아래 규칙을 준수해 주시기 바랍니다.

---

## 📚 목차
1. [📁 파일 이름 규칙](#📁-파일-이름-규칙)
2. [🥷🏿 커밋 메시지 규칙](#🥷🏿-커밋-메시지-규칙)
3. [ 테이블(스키마) 규칙](#테이블-스키마-규칙)

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
