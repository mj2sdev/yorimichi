# Database

### 모델링 규칙 확정
- [ ] 네이밍 컨벤션 확정(스네이크/단수명, PK`id`, FK`{table}_id`)
- [ ] 시간 컬렴 정책 확정(`root`만 `deleted_at`, 자식/조인 테이블은 미사용)
- ENUM/코드값 표준 정의(`feed.status` 등)

### 엔티티 & 슈퍼타입
- [ ] `root` <-> `user/feed/comment/store/food/review` 1:1 식별 관계(PK=FK)로 명시
- [ ] 각 자식 테이블에 `FOREIGN KEY (id) -> root(id)` 주석 표기

### 관계/카디널리티
- [ ] `user`(1)-`feed`(N), `feed`(1)-`comment`(N) 카디널리티 표기
- [ ] `comment` 자기참조(부모-자식) 1:N(옵셔널) 표기(parent_id NULL 허용)
- [ ] `follower`/`user_block` 자기참조 N:M (역할 라벨링: follower/followee, blocker, blockee)
- [ ] `user_like` -> `user`(1)-`root`(N) 다형 대상 관계 표현
- [ ] `notification`에 `actor_user` -> `object(root)` -> `target_user` 3자 관계 주석(화살표/라벨)

### 조인/매핑 테이블(복합 PK)
- [ ] `root_keyword` -> PK(root_id, keyword_id)
- [ ] `root_image` -> PK(root_id, image_id)
- [ ] `feed_user` -> PK(feed_id, user_id)
- [ ] `bookmark` -> PK(user_id, store_id)
- [ ] `store_category` -> PK(store_id, category_id)
- [ ] `food_category` -> PK(food_id, category_id)
- [ ] `store_facility_category` -> PK(store_id, facility_category_id)
- [ ] `road_postal` -> PK(road_id, postal_id)

### 키/제약(ERD 주석으로 명시)
- [ ] UNIQUE: `user.email`
- [ ] UNIQUE: `keyword.name`, `facility_category.name`
- [ ] UNIQUE: `category(parent_cateogry_id, name)`
- [ ] UNIQUE: `user_like(user_id, root_id)`
- [ ] 지역/도로/우편 코드 UNIQUE: `region_sido.code`, `region_sigungu(sido_id, code)`,
                              `region_emd(sigungu_id, code)`, `road(emd_id, code)`, `postal.code`
- [ ] CHECK: `review.rating` 1 ~ 5
- [ ] CHECK: `follower_id <> followee_id`, `blocker_id <> blockee_id`

### 타입/디폴트(ERD 표기)
- [ ] `feed.status` -> `ENUM()`
- [ ] `feed.view_count` -> `DEFAULT 0`
- [ ] 좌표 -> `DECIMAL(10, 7)`

### 인덱스(주요 조회 경로 주석)
- [ ] `feed(user_id, created_at DESC` 또는 `(user_id, id DESC)`
- [ ] `comment(feed_id, parent_id, id)`
- [ ] `review(food_id, id)`
- [ ] `food(store_id, name)`
- [ ] `notification(target_user_id, read_at, created_at)`
- [ ] `user_like(root_id, created_at)`


## ERD

### 공통

- [x] role
  - id
  - name

- [x] root
  - id
  - created_at
  - updated_at
  - deleted_at
  - blinded_at

- [x] keyword
  - id
  - name
  - created_at

- [x] root_keyword
  - root_id
  - keyword_id
  - created_at

- [x] image
  - id
  - url
  - created_at

- [x] root_image
  - root_id
  - image_id
  - created_at


### 유저 관련
- [x] user
  - id
  - role_id
  - email
  - password
  - nickname
  - description
  - last_login_at

- [x] feed
  - id
  - user_id
  - type
  - title
  - content
  - view_count

- [x] feed_coeat
  - feed_id
  - store_id
  - capacity
  - meeting_at
  - auto_accept
  - status

- [x] feed_coeat_request
  - feed_id
  - user_id
  - status
  - message
  - created_at

- [x] comment
  - id
  - user_id
  - feed_id
  - parent_id
  - content

- [X] report
  - reporter_id
  - reported_id
  - description
  - status
  - created_at
  - updated_at

- [X] block
  - blocker_id
  - blockee_id
  - created_at

- [X] follow
  - follower_id
  - followee_id
  - notified
  - created_at

- [X] user_like
  - id
  - user_id
  - root_id
  - created_at

- [X] notification
  - id
  - actor_user_id
  - root_id
  - target_user_id
  - message
  - created_at
  - read_at
  - updated_at


### 상점 관련
- [X] store
  - id
  - address_id
  - name
  - description
  - phone

- [X] bookmark
  - user_id
  - store_id
  - created_at

- [X] food
  - id
  - store_id
  - name
  - price
  - description

- [X] review
  - id
  - user_id
  - food_id
  - rating
  - content

- [X] category
  - id
  - parent_id
  - name
  - created_at

- [X] store_category
  - store_id
  - category_id
  - created_at

- [X] food_category
  - food_id
  - category_id
  - created_at

- [X] facility_category
  - id
  - name
  - created_at

- [X] store_facility_category
  - store_id
  - facility_category_id
  - created_at


### 주소 관련
- [X] address
  - id
  - road_id
  - postal_id
  - detail
  - road_address_text
  - jibun_address_text
  - latitude
  - longitude
  - created_at
  - updated_at

- [X] region_sido
  - id
  - code
  - name

- [X] region_sigungu
  - id
  - sido_id
  - code
  - name

- [X] region_emd
  - id
  - sigungu_id
  - code
  - name

- [X] road
  - id
  - emd_id
  - code
  - name

- [X] postal
  - id
  - code

- [X] road_postal
  - road_id
  - postal_id
  - created_at