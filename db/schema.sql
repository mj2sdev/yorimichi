-- 테이블

-- 주소 관련
CREATE TABLE region_sido (
    id   BIGINT      NOT NULL AUTO_INCREMENT,
    code VARCHAR(10) NOT NULL,
    name VARCHAR(50) NOT NULL,

    CONSTRAINT pk_region_sido      PRIMARY KEY (id),
    CONSTRAINT uk_region_sido_code UNIQUE      (code),
    CONSTRAINT uk_region_sido_name UNIQUE      (name)
);

CREATE TABLE region_sigungu (
    id      BIGINT      NOT NULL AUTO_INCREMENT,
    sido_id BIGINT      NOT NULL,
    code    VARCHAR(10) NOT NULL,
    name    VARCHAR(50) NOT NULL,

    CONSTRAINT pk_region_sigungu           PRIMARY KEY (id),
    CONSTRAINT fk_region_sigungu_sido      FOREIGN KEY (sido_id) REFERENCES region_sido(id),
    CONSTRAINT uk_region_sigungu_code      UNIQUE      (code),
    CONSTRAINT uk_region_sigungu_sido_name UNIQUE      (sido_id, name)
);

CREATE TABLE region_emd (
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    sigungu_id BIGINT      NOT NULL,
    code       VARCHAR(10) NOT NULL,
    name       VARCHAR(50) NOT NULL,

    CONSTRAINT pk_region_emd              PRIMARY KEY (id),
    CONSTRAINT fk_region_emd_sigungu      FOREIGN KEY (sigungu_id) REFERENCES region_sigungu(id),
    CONSTRAINT uk_region_emd_code         UNIQUE      (code),
    CONSTRAINT uk_region_emd_sigungu_name UNIQUE      (sigungu_id, name)
);

CREATE TABLE road (
    id     BIGINT      NOT NULL AUTO_INCREMENT,
    emd_id BIGINT      NOT NULL,
    code   VARCHAR(10) NOT NULL,
    name   VARCHAR(50) NOT NULL,

    CONSTRAINT pk_road          PRIMARY KEY (id),
    CONSTRAINT fk_road_emd      FOREIGN KEY (emd_id) REFERENCES region_emd(id),
    CONSTRAINT uk_road_code     UNIQUE      (code),
    CONSTRAINT uk_road_emd_name UNIQUE      (emd_id, name)
);

CREATE TABLE postal (
    id   BIGINT     NOT NULL AUTO_INCREMENT,
    code VARCHAR(5) NOT NULL,

    CONSTRAINT pk_postal      PRIMARY KEY (id),
    CONSTRAINT uk_postal_code UNIQUE      (code)
);

CREATE TABLE road_postal (
    road_id    BIGINT   NOT NULL,
    postal_id  BIGINT   NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_road_postal        PRIMARY KEY (road_id, postal_id),
    CONSTRAINT fk_road_postal_road   FOREIGN KEY (road_id)   REFERENCES road(id)   ON DELETE CASCADE,
    CONSTRAINT fk_road_postal_postal FOREIGN KEY (postal_id) REFERENCES postal(id) ON DELETE CASCADE
);

CREATE TABLE address (
    id                 BIGINT         NOT NULL AUTO_INCREMENT,
    road_id            BIGINT         NOT NULL,
    postal_id          BIGINT         NOT NULL,
    detail             TEXT           NOT NULL,
    road_address_text  TEXT           NOT NULL,
    jibun_address_text TEXT           NOT NULL,
    latitude           DECIMAL(9, 6)  NOT NULL,
    longitude          DECIMAL(10, 6) NOT NULL,
    created_at         DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_address             PRIMARY KEY (id),
    CONSTRAINT fk_address_road        FOREIGN KEY (road_id)            REFERENCES road(id),
    CONSTRAINT fk_address_postal      FOREIGN KEY (postal_id)          REFERENCES postal(id),
    CONSTRAINT fk_address_road_postal FOREIGN KEY (road_id, postal_id) REFERENCES road_postal(road_id, postal_id),

    -- 범위 검증
    CONSTRAINT ck_address_lat CHECK (latitude BETWEEN -90 AND 90),
    CONSTRAINT ck_address_lon CHECK (longitude BETWEEN -180 AND 180)
);


-- 공통
CREATE TABLE root (
    id         BIGINT   NOT NULL AUTO_INCREMENT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME,
    blinded_at DATETIME,

    CONSTRAINT pk_root PRIMARY KEY (id)
);

CREATE TABLE role (
    id   BIGINT                NOT NULL AUTO_INCREMENT,
    name ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',

    CONSTRAINT pk_role      PRIMARY KEY (id),
    CONSTRAINT uk_role_name UNIQUE      (name)
);


CREATE TABLE keyword (
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    name       VARCHAR(20) NOT NULL,
    created_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_keyword      PRIMARY KEY (id),
    CONSTRAINT uk_keyword_name UNIQUE      (name)
);

CREATE TABLE image (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    url        VARCHAR(255) NOT NULL,
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_image PRIMARY KEY (id)
);

CREATE TABLE root_keyword (
    root_id    BIGINT   NOT NULL,
    keyword_id BIGINT   NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_root_keyword         PRIMARY KEY (root_id, keyword_id),
    CONSTRAINT fk_root_keyword_root    FOREIGN KEY (root_id)    REFERENCES root(id)    ON DELETE CASCADE,
    CONSTRAINT fk_root_keyword_keyword FOREIGN KEY (keyword_id) REFERENCES keyword(id) ON DELETE CASCADE
);

CREATE TABLE root_image (
    root_id    BIGINT   NOT NULL,
    image_id   BIGINT   NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_root_image       PRIMARY KEY (root_id, image_id),
    CONSTRAINT fk_root_image_root  FOREIGN KEY (root_id)  REFERENCES root(id)  ON DELETE CASCADE,
    CONSTRAINT fk_root_image_image FOREIGN KEY (image_id) REFERENCES image(id) ON DELETE CASCADE
);

-- 상점 관련
CREATE TABLE store (
    id          BIGINT       NOT NULL,
    address_id  BIGINT       NOT NULL,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    phone       VARCHAR(20),

    CONSTRAINT pk_store              PRIMARY KEY (id),
    CONSTRAINT fk_store_root         FOREIGN KEY (id)         REFERENCES root(id),
    CONSTRAINT fk_store_address      FOREIGN KEY (address_id) REFERENCES address(id),
    CONSTRAINT uk_store_address_name UNIQUE      (address_id, name)
);

CREATE TABLE category (
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    parent_id  BIGINT,
    name       VARCHAR(50) NOT NULL,
    created_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_category          PRIMARY KEY (id),
    CONSTRAINT fk_category_parent   FOREIGN KEY (parent_id) REFERENCES category(id) ON DELETE SET NULL,
    CONSTRAINT uk_category_name     UNIQUE      (name),
    CONSTRAINT ck_category_not_self CHECK       (parent_id IS NULL OR parent_id <> id)
);

CREATE TABLE store_category (
    store_id    BIGINT   NOT NULL,
    category_id BIGINT   NOT NULL,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_store_category          PRIMARY KEY (store_id, category_id),
    CONSTRAINT fk_store_category_store    FOREIGN KEY (store_id)    REFERENCES store(id)    ON DELETE CASCADE,
    CONSTRAINT fk_store_category_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE
);

CREATE TABLE facility_category (
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    name       VARCHAR(50) NOT NULL,
    created_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_facility_category      PRIMARY KEY (id),
    CONSTRAINT uk_facility_category_name UNIQUE      (name)
);

CREATE TABLE store_facility_category (
    store_id             BIGINT   NOT NULL,
    facility_category_id BIGINT   NOT NULL,
    created_at           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_store_facility_category          PRIMARY KEY (store_id, facility_category_id),
    CONSTRAINT fk_store_facility_category_store    FOREIGN KEY (store_id)             REFERENCES store(id)             ON DELETE CASCADE,
    CONSTRAINT fk_store_facility_category_category FOREIGN KEY (facility_category_id) REFERENCES facility_category(id) ON DELETE CASCADE
);

CREATE TABLE food (
    id          BIGINT      NOT NULL,
    store_id    BIGINT      NOT NULL,
    name        VARCHAR(50) NOT NULL,
    price       INT         NOT NULL,
    description TEXT,

    CONSTRAINT pk_food            PRIMARY KEY (id),
    CONSTRAINT fk_food_root       FOREIGN KEY (id)       REFERENCES root(id),
    CONSTRAINT fk_food_store      FOREIGN KEY (store_id) REFERENCES store(id) ON DELETE CASCADE,
    CONSTRAINT uk_food_store_name UNIQUE      (store_id, name),
    CONSTRAINT ck_food_price      CHECK       (price >= 0)
);

CREATE TABLE food_category (
    food_id     BIGINT   NOT NULL,
    category_id BIGINT   NOT NULL,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_food_category          PRIMARY KEY (food_id, category_id),
    CONSTRAINT fk_food_category_food     FOREIGN KEY (food_id)     REFERENCES food(id)     ON DELETE CASCADE,
    CONSTRAINT fk_food_category_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE
);

-- 유저
CREATE TABLE user (
    id            BIGINT       NOT NULL,
    role_id       BIGINT       NOT NULL,
    email         VARCHAR(100) NOT NULL,
    password      VARCHAR(256) NOT NULL,
    nickname      VARCHAR(20)  NOT NULL,
    description   TEXT,
    last_login_at DATETIME,

    CONSTRAINT pk_user          PRIMARY KEY (id),
    CONSTRAINT fk_user_root     FOREIGN KEY (id)      REFERENCES root(id),
    CONSTRAINT fk_user_role     FOREIGN KEY (role_id) REFERENCES role(id),
    CONSTRAINT uk_user_email    UNIQUE      (email),
    CONSTRAINT uk_user_nickname UNIQUE      (nickname)
);

CREATE TABLE bookmark (
    user_id    BIGINT   NOT NULL,
    store_id   BIGINT   NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_bookmark       PRIMARY KEY (user_id, store_id),
    CONSTRAINT fk_bookmark_user  FOREIGN KEY (user_id)  REFERENCES user(id)  ON DELETE CASCADE,
    CONSTRAINT fk_bookmark_store FOREIGN KEY (store_id) REFERENCES store(id) ON DELETE CASCADE
);

CREATE TABLE review (
    id      BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    food_id BIGINT NOT NULL,
    rating  INT    NOT NULL,
    content TEXT,

    CONSTRAINT pk_review        PRIMARY KEY (id),
    CONSTRAINT fk_review_root   FOREIGN KEY (id)      REFERENCES root(id),
    CONSTRAINT fk_review_user   FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_review_food   FOREIGN KEY (food_id) REFERENCES food(id) ON DELETE CASCADE,
    -- CONSTRAINT uk_review_user_food UNIQUE (user_id, food_id) -- 사용자당 음식 1건만 리뷰 제약
    CONSTRAINT ck_review_rating CHECK       (rating BETWEEN 1 AND 5)
);

CREATE TABLE feed (
    id         BIGINT                NOT NULL,
    user_id    BIGINT                NOT NULL,
    type       ENUM('FEED', 'COEAT') NOT NULL DEFAULT 'FEED',
    title      VARCHAR(100)          NOT NULL,
    content    TEXT                  NOT NULL,
    view_count INT                   NOT NULL DEFAULT 0,

    CONSTRAINT pk_feed      PRIMARY KEY (id),
    CONSTRAINT fk_feed_root FOREIGN KEY (id)      REFERENCES root(id),
    CONSTRAINT fk_feed_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

CREATE TABLE feed_coeat (
    feed_id     BIGINT                         NOT NULL,
    store_id    BIGINT,
    capacity    INT                            NOT NULL,
    meeting_at  DATETIME                       NOT NULL,
    auto_accept BOOLEAN                        NOT NULL DEFAULT FALSE,
    status ENUM('OPEN', 'CLOSED', 'CANCELLED') NOT NULL DEFAULT 'OPEN',

    CONSTRAINT pk_feed_coeat          PRIMARY KEY (feed_id),
    CONSTRAINT fk_feed_coeat_feed     FOREIGN KEY (feed_id)  REFERENCES feed(id)  ON DELETE CASCADE,
    CONSTRAINT fk_feed_coeat_store    FOREIGN KEY (store_id) REFERENCES store(id) ON DELETE SET NULL,
    CONSTRAINT ck_feed_coeat_capacity CHECK       (capacity >= 1)
);

CREATE TABLE feed_coeat_request (
    feed_id    BIGINT                                               NOT NULL,
    user_id    BIGINT                                               NOT NULL,
    status     ENUM('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    message    TEXT                                                 NOT NULL,
    -- reject_reason VARCHAR(200) -- 거절 사유
    created_at DATETIME                                             NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_feed_coeat_request      PRIMARY KEY (feed_id, user_id),
    CONSTRAINT fk_feed_coeat_request_feed FOREIGN KEY (feed_id) REFERENCES feed_coeat(feed_id) ON DELETE CASCADE,
    CONSTRAINT fk_feed_coeat_request_user FOREIGN KEY (user_id) REFERENCES user(id)            ON DELETE CASCADE
);

CREATE TABLE comment (
    id        BIGINT NOT NULL,
    user_id   BIGINT NOT NULL,
    feed_id   BIGINT NOT NULL,
    parent_id BIGINT,
    content   TEXT   NOT NULL,

    CONSTRAINT pk_comment                 PRIMARY KEY (id),
    CONSTRAINT fk_comment_root            FOREIGN KEY (id)        REFERENCES root(id),
    CONSTRAINT fk_comment_user            FOREIGN KEY (user_id)   REFERENCES user(id)    ON DELETE CASCADE,
    CONSTRAINT fk_comment_feed            FOREIGN KEY (feed_id)   REFERENCES feed(id)    ON DELETE CASCADE,
    CONSTRAINT fk_comment_parent          FOREIGN KEY (parent_id) REFERENCES comment(id) ON DELETE SET NULL,

    -- 자기 자신을 부모로 참조 금지
    CONSTRAINT ck_comment_not_self_parent CHECK       (parent_id IS NULL OR parent_id <> id)
);

CREATE TABLE report (
    reporter_id BIGINT                                                            NOT NULL,
    reported_id BIGINT                                                            NOT NULL,
    description TEXT                                                              NOT NULL,
    status      ENUM('PENDING', 'IN_REVIEW', 'RESOLVED', 'REJECTED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    created_at  DATETIME                                                          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME                                                          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_report          PRIMARY KEY (reporter_id, reported_id),
    CONSTRAINT fk_report_reporter FOREIGN KEY (reporter_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_report_reported FOREIGN KEY (reported_id) REFERENCES user(id) ON DELETE CASCADE
);

CREATE TABLE block (
    blocker_id BIGINT   NOT NULL,
    blockee_id BIGINT   NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_block          PRIMARY KEY (blocker_id, blockee_id),
    CONSTRAINT fk_block_blocker  FOREIGN KEY (blocker_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_block_blockee  FOREIGN KEY (blockee_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT ck_block_not_self CHECK       (blocker_id <> blockee_id)
);

CREATE TABLE follow (
    follower_id BIGINT   NOT NULL,
    followee_id BIGINT   NOT NULL,
    notified    BOOLEAN  NOT NULL DEFAULT FALSE,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_follow          PRIMARY KEY (follower_id, followee_id),
    CONSTRAINT fk_follow_follower FOREIGN KEY (follower_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_follow_followee FOREIGN KEY (followee_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT ck_follow_not_self CHECK       (follower_id <> followee_id)
);

CREATE TABLE user_like (
    user_id    BIGINT NOT NULL,
    root_id    BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_user_like      PRIMARY KEY (user_id, root_id),
    CONSTRAINT fk_user_like_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_like_root FOREIGN KEY (root_id) REFERENCES root(id) ON DELETE CASCADE
);

CREATE TABLE notification (
    actor_user_id  BIGINT   NOT NULL,
    target_user_id BIGINT   NOT NULL,
    root_id        BIGINT   NOT NULL,
    message        TEXT     NOT NULL,
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    read_at        DATETIME,
    updated_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_notification             PRIMARY KEY (actor_user_id, target_user_id, root_id),
    CONSTRAINT fk_notification_actor_user  FOREIGN KEY (actor_user_id)  REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_notification_target_user FOREIGN KEY (target_user_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_notification_root        FOREIGN KEY (root_id)        REFERENCES root(id) ON DELETE CASCADE,

    -- 자기 자신에게 보낸 알림 금지
    CONSTRAINT ck_notification_not_self    CHECK       (actor_user_id <> target_user_id)
);


-- 인덱스

-- 지역/주소
-- 지역 시/군/구를 시/도+이름으로 탐색할 때 사용
-- ex) 특정 시/도의 "강남구" 찾기: SELECT id FROM region_sigungu WHERE sido_id=? AND name=?;
-- ⚠️ 참고: (sido_id, name)에는 UNIQUE도 있으므로 중복 인덱스가 된다면 이 인덱스는 생략 가능
CREATE INDEX idx_region_sigungu_sido_id_name   ON region_sigungu (sido_id, name);

-- 읍/면/동을 시/군/구+이름으로 탐색
-- ex) SELECT id FROM region_emd WHERE sigungu_id=? AND name=?;
-- ⚠️ (sigungu_id, name)에도 UNIQUE가 있으니 중복이라면 생략 가능
CREATE INDEX idx_region_emd_sigungu_id_name    ON region_emd     (sigungu_id, name);

-- 우편번호로 가능한 도로(road)들 역탐색 (주소 생성/검증 시 조인 최적화)
-- ex) SELECT rp.road_id FROM road_postal rp WHERE rp.postal_id=?;
CREATE INDEX idx_road_postal_postal_id_road_id ON road_postal    (postal_id, road_id);

-- 도로 기준 주소 나열 / 최신순 정렬
-- ex) SELECT * FROM address WHERE road_id=? ORDER BY id DESC LIMIT 50;
CREATE INDEX idx_address_road_id               ON address        (road_id);

-- 우편번호 기준 주소 나열 / 최신순 정렬
-- ex) SELECT * FROM address WHERE postal_id=? ORDER BY id DESC LIMIT 50;
CREATE INDEX idx_address_postal_id             ON address        (postal_id);


-- 카테고리/편의/매핑
-- 부모→자식 카테고리 트리 탐색
-- ex) SELECT * FROM category WHERE parent_id=? ORDER BY name;
-- ⚠️ FK(parent_id)는 InnoDB가 자동 인덱스를 만들 수도 있지만, 명시적으로 이름을 갖춘 인덱스로 관리
CREATE INDEX idx_category_parent_id                                    ON category                (parent_id);

-- 카테고리별 상점 목록(카테고리→상점 역방향). PK(store_id,category_id)만으로는 category_id 선두 검색이 느림
-- ex) SELECT store_id FROM store_category WHERE category_id=?;
CREATE INDEX idx_store_category_category_id_store_id                   ON store_category          (category_id, store_id);

-- 카테고리별 음식 목록(역방향)
-- ex) SELECT food_id FROM food_category WHERE category_id=?;
CREATE INDEX idx_food_category_category_id_food_id                     ON food_category           (category_id, food_id);

-- 편의시설별 상점 목록(역방향)
-- ex) SELECT store_id FROM store_facility_category WHERE facility_category_id=?;
CREATE INDEX idx_store_facility_category_facility_category_id_store_id ON store_facility_category (facility_category_id, store_id);


-- 상점/음식/리뷰
-- 주소 단위 상점 조회
-- ex) SELECT * FROM store WHERE address_id=? ORDER BY name;
CREATE INDEX idx_store_address_id  ON store  (address_id);

-- 상점의 메뉴 나열 + 최신/ID 정렬
-- ex) SELECT * FROM food WHERE store_id=? ORDER BY id DESC LIMIT 100;
CREATE INDEX idx_food_store_id_id  ON food   (store_id, id);

-- 음식별 리뷰 나열 + ID(시간 대용) 정렬
-- ex) SELECT * FROM review WHERE food_id=? ORDER BY id DESC LIMIT 100;
CREATE INDEX idx_review_food_id_id ON review (food_id, id);

-- 사용자가 쓴 리뷰 목록 + ID 정렬
-- ex) SELECT * FROM review WHERE user_id=? ORDER BY id DESC LIMIT 100;
CREATE INDEX idx_review_user_id_id ON review (user_id, id);


-- 피드/모임/신청
-- 사용자의 피드 목록 + ID 정렬
-- ex) SELECT * FROM feed WHERE user_id=? ORDER BY id DESC LIMIT 30;
CREATE INDEX idx_feed_user_id_id                              ON feed               (user_id, id);

-- 타입(피드/모임)별 목록 + ID 정렬
-- ex) SELECT * FROM feed WHERE type='FEED' ORDER BY id DESC LIMIT 30;
CREATE INDEX idx_feed_type_id                                 ON feed               (type, id);

-- 매장별 모임 탐색 + 상태/모임일시 필터/정렬
-- ex) SELECT * FROM feed_coeat WHERE store_id=? AND status='OPEN' AND meeting_at>=? ORDER BY meeting_at;
CREATE INDEX idx_feed_coeat_store_id_status_meeting_at        ON feed_coeat         (store_id, status, meeting_at);

-- 모임별 신청 현황(상태/시간) 조회
-- ex) SELECT * FROM feed_coeat_request WHERE feed_id=? AND status IN ('PENDING','APPROVED') ORDER BY created_at DESC;
CREATE INDEX idx_feed_coeat_request_feed_id_status_created_at ON feed_coeat_request (feed_id, status, created_at);

-- 내가 신청한 모임들 최신순
-- ex) SELECT * FROM feed_coeat_request WHERE user_id=? ORDER BY created_at DESC;
CREATE INDEX idx_feed_coeat_request_user_id_created_at        ON feed_coeat_request (user_id, created_at);


-- 관계/좋아요/알림/신고
-- 내 팔로워(나를 팔로우하는 사람) 목록
-- ex) SELECT follower_id FROM follow WHERE followee_id=? ORDER BY follower_id;
CREATE INDEX idx_follow_followee_id_follower_id                 ON follow       (followee_id, follower_id);

-- 누가 나를 차단했는지 역방향 조회
-- ex) SELECT blocker_id FROM block WHERE blockee_id=?;
CREATE INDEX idx_block_blockee_id_blocker_id                    ON block        (blockee_id, blocker_id);

-- 컨텐츠(=root)별 좋아요 수/체크
-- ex) SELECT COUNT(*) FROM user_like WHERE root_id=?;
-- ex) SELECT 1 FROM user_like WHERE root_id=? AND user_id=?;
CREATE INDEX idx_user_like_root_id_user_id                      ON user_like    (root_id, user_id);

-- 내 알림: 미읽음 우선 + 최신순(역순 스캔)
-- ex) SELECT * FROM notification WHERE target_user_id=? AND read_at IS NULL ORDER BY created_at DESC LIMIT 50;
CREATE INDEX idx_notification_target_user_id_read_at_created_at ON notification (target_user_id, read_at, created_at);

-- 내가 보낸 알림 최신순
-- ex) SELECT * FROM notification WHERE actor_user_id=? ORDER BY created_at DESC LIMIT 50;
CREATE INDEX idx_notification_actor_user_id_created_at          ON notification (actor_user_id, created_at);

-- 특정 사용자가 신고된 건들 상태별 조회 + 최신순
-- ex) SELECT * FROM report WHERE reported_id=? AND status IN ('PENDING','IN_REVIEW') ORDER BY created_at DESC;
CREATE INDEX idx_report_reported_id_status_created_at           ON report       (reported_id, status, created_at);


-- 댓글
-- 피드의 댓글 트리 조회: 부모 NULL(최상위) 먼저, 또는 특정 부모의 자식 조회
-- ex) SELECT * FROM comment WHERE feed_id=? AND parent_id IS NULL ORDER BY id;
-- ex) SELECT * FROM comment WHERE feed_id=? AND parent_id=? ORDER BY id;
CREATE INDEX idx_comment_feed_id_parent_id_id ON comment (feed_id, parent_id, id);

-- 사용자의 댓글 목록
-- ex) SELECT * FROM comment WHERE user_id=? ORDER BY id DESC LIMIT 50;
CREATE INDEX idx_comment_user_id_id           ON comment (user_id, id);