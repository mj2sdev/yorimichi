# CASCADE vs SET NULL 사용 구분 (현재 스키마 예시 기반)

이 문서는 **현재 테이블 정의**를 기준으로, 언제 `ON DELETE CASCADE`를 쓰고 언제 `ON DELETE SET NULL`을 쓰는지 **원칙/사례/주의점**을 한 번에 정리한 가이드입니다. 마지막에 **스키마별 매핑 표**도 포함되어 있습니다.

---

## 1) 한눈에 보는 원칙

### 언제 `CASCADE`?
- **완전 종속(child-only) 데이터**: 부모 없이는 의미가 없는 파생/연결/로그.
- **N:M 연결 테이블**: 부모가 없어지면 연결도 함께 정리(고아 방지).
- **정책상 기록 보존 불필요**: 부모 삭제 시 함께 지워져도 무방.

> 예) `root_image`, `root_keyword`, `store_category`, `food_category`, `store_facility_category`, `bookmark`, `follow`, `block`, `user_like`, `notification`, `feed_coeat_request`, `comment.feed_id`, `review.user_id/food_id` 등.

### 언제 `SET NULL`?
- **관계는 끊어도 자식 레코드는 보존**해야 할 때.
- **자기참조 트리**: 상위가 삭제돼도 하위를 고아로 두고 싶지 않을 때(최상위로 승격).
- **참조 대상이 폐업/비공개 등으로 사라져도 기록은 남겨야** 할 때.

> 예) `category.parent_id`(삭제 시 하위 카테고리 최상위 승격),  
> `comment.parent_id`(부모 댓글 삭제 시 자식 댓글의 부모를 NULL),  
> `feed_coeat.store_id`(가게 삭제/폐업 시 모임 자체 기록은 유지).

### 언제 **RESTRICT/NO ACTION**(기본) 유지?
- **마스터(참조) 데이터**: 주소/행정구역/도로/우편 등 기준 정보.
- **슈퍼타입-서브타입(root↔자식)의 생명주기를 코드/운영으로 통제**할 때(소프트 삭제 등).

> 예) `store.address_id → address.id`, `food.id → root.id`, `user.id → root.id` 등은 기본(RESTRICT)로 두어 **실수로 부모를 먼저 지우지 못하게** 함.

---

## 2) 현재 스키마에 적용된 구분 요약

| 영역 | FK | ON DELETE | 이유(요약) |
|---|---|---:|---|
| **지역/주소** | `road_postal.road_id → road.id` / `postal_id → postal.id` | **CASCADE** | 도로/우편 삭제 시 매핑 자동 정리(고아 방지) |
|  | `address.road_id/postal_id/ (road_id,postal_id)` | **RESTRICT** | 실제 주소는 함부로 같이 지우지 않음(데이터 보전) |
| **root 연결** | `root_image`, `root_keyword` | **CASCADE** | 본문(루트) 삭제 시 첨부/태그도 함께 삭제 |
| **상점** | `store.address_id → address.id` | **RESTRICT** | 주소는 기준 데이터, 안전하게 막음 |
| **카테고리 트리** | `category.parent_id → category.id` | **SET NULL** | 상위 삭제 시 하위는 최상위 승격(보존) |
| **카테고리/편의 매핑** | `store_category`, `food_category`, `store_facility_category` | **CASCADE** | 매핑은 파생 연결, 부모 삭제 시 정리 |
| **음식** | `food.store_id → store.id` | **CASCADE** | 상점 삭제 시 메뉴도 제거가 자연스러움 |
| **리뷰** | `review.user_id/food_id` | **CASCADE** | 유저/음식 삭제 시 관련 리뷰 정리 |
| **피드** | `feed.user_id` | **CASCADE** | 유저 삭제 시 피드 정리 |
| **모임** | `feed_coeat.feed_id` | **CASCADE** | 피드 삭제 시 모임 정리 |
|  | `feed_coeat.store_id` | **SET NULL** | 가게 삭제/폐업에도 모임 기록 보존(연결만 해제) |
| **모임 신청** | `feed_coeat_request.feed_id/user_id` | **CASCADE** | 모임/유저 삭제 시 신청 정리 |
| **댓글** | `comment.feed_id/user_id` | **CASCADE** | 피드/유저 삭제 시 댓글 정리 |
|  | `comment.parent_id → comment.id` | **SET NULL** | 부모 삭제돼도 자식 스레드 유지 |
| **관계/알림/신고** | `bookmark/follow/block/user_like` | **CASCADE** | 당사자/대상 삭제 시 연결 정리 |
|  | `notification.actor/target/root` | **CASCADE** | 주체/수신자/대상 삭제 시 알림 정리 |
|  | `report.reporter_id/reported_id` | **CASCADE** | 당사자 삭제 시 신고 정리 |

> 참고: `root` 슈퍼타입을 **소프트 삭제**로 운용한다면 물리 삭제는 드물고, 삭제 연산은 가능하면 **root에서 시작**하는 조직적 규칙을 권장합니다.

---

## 3) 결정 체크리스트

- **자식만으로 비즈니스 의미가 있나?**  
  - 없다 → **CASCADE**  
  - 있다 → **SET NULL** 또는 **RESTRICT**
- **기록 보존/감사 로그가 필요한가?**  
  - 필요 → **SET NULL** 또는 **RESTRICT** + 별도 아카이브
- **대량 삭제 위험이 있는가?**(자기참조/깊은 트리)  
  - 있다 → **SET NULL** 우선 검토(안전)
- **UI/쿼리에서 고아 레코드를 처리할 수 있나?**  
  - 없다 → **CASCADE**로 고아 방지
- **컬럼이 NULL 허용인가?**  
  - SET NULL은 **반드시 NULL 허용**이어야 함

---

## 4) 실제 SQL 패턴

### CASCADE 예시
```sql
CONSTRAINT fk_food_category_food
FOREIGN KEY (food_id) REFERENCES food(id) ON DELETE CASCADE;
```

### SET NULL 예시
```sql
CONSTRAINT fk_comment_parent
FOREIGN KEY (parent_id) REFERENCES comment(id) ON DELETE SET NULL;
```

### RESTRICT(기본) 명시(선택)
```sql
CONSTRAINT fk_store_address
FOREIGN KEY (address_id) REFERENCES address(id) ON DELETE RESTRICT;
```

> **주의:** `SET NULL`을 적용하려면 해당 컬럼(`parent_id` 등)이 **NULL 허용**이어야 합니다.

---

## 5) 자주 묻는 함정/주의

- **자기참조 + CASCADE**는 **연쇄 대량 삭제** 위험.  
  트리/스레드는 가급적 **SET NULL**(혹은 논리 삭제) 채택.
- **루트-서브타입**: 현재 구조는 자식이 `root`를 참조.  
  `root`를 물리 삭제하면 **FK RESTRICT로 에러** → 먼저 자식 삭제/이관 후 `root` 삭제.  
  (실무에선 **소프트 삭제**로 운용)
- **성능**: CASCADE는 삭제 시 자식 스캔이 필요. FK 컬럼엔 **인덱스 필수**(InnoDB는 기본 생성).  
  하지만 **쿼리 패턴별 복합 인덱스**는 직접 정의해야 함(이미 인덱스 세트 반영 완료).

---

## 6) 변경 가이드(정책 바뀔 때)

- **CASCADE → SET NULL** 변경 시:  
  1) 컬럼 NULL 허용 확인/변경  
  2) FK 드롭 후 `ON DELETE SET NULL`로 재생성
- **SET NULL → CASCADE** 변경 시:  
  1) 고아 허용 여부/데이터 정리  
  2) FK 드롭 후 `ON DELETE CASCADE`로 재생성
- **RESTRICT ↔ CASCADE/SET NULL**:  
  운영/정책 리뷰를 거쳐 변경. 데이터 마이그레이션 스크립트 동반 권장.

---

## 7) 부록: 현재 FK 매핑(요약)

```text
[지역/주소]
road_postal.road_id    → road.id      : CASCADE
road_postal.postal_id  → postal.id    : CASCADE
address.road_id        → road.id      : RESTRICT
address.postal_id      → postal.id    : RESTRICT
address.(road_id,postal_id) → road_postal : RESTRICT

[root 연결]
root_image.root_id     → root.id      : CASCADE
root_image.image_id    → image.id     : CASCADE
root_keyword.root_id   → root.id      : CASCADE
root_keyword.keyword_id→ keyword.id   : CASCADE

[상점/카테고리/편의]
store.address_id       → address.id   : RESTRICT
store_category.store_id→ store.id     : CASCADE
store_category.category_id → category.id : CASCADE
store_facility_category.store_id → store.id : CASCADE
store_facility_category.facility_category_id → facility_category.id : CASCADE
category.parent_id     → category.id  : SET NULL

[음식/리뷰]
food.store_id          → store.id     : CASCADE
food_category.food_id  → food.id      : CASCADE
food_category.category_id → category.id : CASCADE
review.user_id         → user.id      : CASCADE
review.food_id         → food.id      : CASCADE

[피드/모임/신청/댓글]
feed.user_id           → user.id      : CASCADE
feed_coeat.feed_id     → feed.id      : CASCADE
feed_coeat.store_id    → store.id     : SET NULL
feed_coeat_request.feed_id → feed_coeat.feed_id : CASCADE
feed_coeat_request.user_id → user.id  : CASCADE
comment.feed_id        → feed.id      : CASCADE
comment.user_id        → user.id      : CASCADE
comment.parent_id      → comment.id   : SET NULL

[관계/알림/신고]
bookmark.user_id       → user.id      : CASCADE
bookmark.store_id      → store.id     : CASCADE
follow.follower_id     → user.id      : CASCADE
follow.followee_id     → user.id      : CASCADE
block.blocker_id       → user.id      : CASCADE
block.blockee_id       → user.id      : CASCADE
user_like.user_id      → user.id      : CASCADE
user_like.root_id      → root.id      : CASCADE
notification.actor_user_id  → user.id : CASCADE
notification.target_user_id → user.id : CASCADE
notification.root_id   → root.id      : CASCADE
report.reporter_id     → user.id      : CASCADE
report.reported_id     → user.id      : CASCADE
```

---

## 8) 결론

- **지금 설정은 실무적으로 매우 적절**합니다.  
- `CASCADE`는 **연결/파생 데이터 정리**, `SET NULL`은 **기록 보존/트리 유지**, `RESTRICT`는 **마스터/기준 데이터 보호**에 사용.  
- 정책 변경 시 본 문서의 **체크리스트 → 변경 가이드** 순으로 적용하면 안전합니다.