-- 테이블

-- 주소 관련
CREATE TABLE region_sido (
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    code       VARCHAR(10) NOT NULL,
    name       VARCHAR(50) NOT NULL,
    created_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_region_sido      PRIMARY KEY (id),
    CONSTRAINT uk_region_sido_code UNIQUE      (code),
    CONSTRAINT uk_region_sido_name UNIQUE      (name)
);

CREATE TABLE region_sigungu (
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    sido_id    BIGINT      NOT NULL,
    code       VARCHAR(10) NOT NULL,
    name       VARCHAR(50) NOT NULL,
    created_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

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
    created_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_region_emd              PRIMARY KEY (id),
    CONSTRAINT fk_region_emd_sigungu      FOREIGN KEY (sigungu_id) REFERENCES region_sigungu(id),
    CONSTRAINT uk_region_emd_code         UNIQUE      (code),
    CONSTRAINT uk_region_emd_sigungu_name UNIQUE      (sigungu_id, name)
);

/*CREATE TABLE road (
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    emd_id     BIGINT      NOT NULL,
    code       VARCHAR(10) NOT NULL,
    name       VARCHAR(50) NOT NULL,
    created_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_road          PRIMARY KEY (id),
    CONSTRAINT fk_road_emd      FOREIGN KEY (emd_id) REFERENCES region_emd(id),
    CONSTRAINT uk_road_code     UNIQUE      (code),
    CONSTRAINT uk_road_emd_name UNIQUE      (emd_id, name)
);

CREATE TABLE postal (
    id         BIGINT     NOT NULL AUTO_INCREMENT,
    code       VARCHAR(5) NOT NULL,
    created_at DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME   NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_postal      PRIMARY KEY (id),
    CONSTRAINT uk_postal_code UNIQUE      (code)
);

CREATE TABLE road_postal (
    road_id    BIGINT   NOT NULL,
    postal_id  BIGINT   NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_road_postal        PRIMARY KEY (road_id, postal_id),
    CONSTRAINT fk_road_postal_road   FOREIGN KEY (road_id)   REFERENCES road(id)   ON DELETE CASCADE,
    CONSTRAINT fk_road_postal_postal FOREIGN KEY (postal_id) REFERENCES postal(id) ON DELETE CASCADE
);*/

CREATE TABLE address (
    id                 BIGINT      NOT NULL AUTO_INCREMENT,
    emd_id             BIGINT      NOT NULL,
--     road_id            BIGINT         NOT NULL,
--     postal_id          BIGINT         NOT NULL,
    detail             TEXT        NOT NULL,
    road_address_text  TEXT        NOT NULL,
    jibun_address_text TEXT        NOT NULL,
    place_id           VARCHAR(64) NOT NULL,
--     latitude           DECIMAL(9, 6)  NOT NULL,
--     longitude          DECIMAL(10, 6) NOT NULL,
    created_at         DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_address          PRIMARY KEY (id),
    CONSTRAINT fk_address_emd      FOREIGN KEY (emd_id) REFERENCES region_emd(id),
    CONSTRAINT uk_address_place_id UNIQUE (place_id)
--     CONSTRAINT fk_address_road        FOREIGN KEY (road_id)            REFERENCES road(id),
--     CONSTRAINT fk_address_postal      FOREIGN KEY (postal_id)          REFERENCES postal(id),
--     CONSTRAINT fk_address_road_postal FOREIGN KEY (road_id, postal_id) REFERENCES road_postal(road_id, postal_id),

    -- 범위 검증
--     CONSTRAINT ck_address_lat CHECK (latitude BETWEEN -90 AND 90),
--     CONSTRAINT ck_address_lon CHECK (longitude BETWEEN -180 AND 180)
);


-- 공통
CREATE TABLE root (
    id         BIGINT   NOT NULL AUTO_INCREMENT,
    type       ENUM('USER', 'STORE', 'FOOD', 'COEAT', 'COMMENT', 'REVIEW') NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at DATETIME,
    blinded_at DATETIME,

    CONSTRAINT pk_root PRIMARY KEY (id)
);

CREATE TABLE role (
    id         BIGINT                NOT NULL AUTO_INCREMENT,
    name       ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
    created_at DATETIME              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_role      PRIMARY KEY (id),
    CONSTRAINT uk_role_name UNIQUE      (name)
);


CREATE TABLE keyword (
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    name       VARCHAR(20) NOT NULL,
    created_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_keyword      PRIMARY KEY (id),
    CONSTRAINT uk_keyword_name UNIQUE      (name)
);

CREATE TABLE image (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    url        VARCHAR(255) NOT NULL,
    created_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_image PRIMARY KEY (id)
);

CREATE TABLE root_keyword (
    root_id    BIGINT   NOT NULL,
    keyword_id BIGINT   NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_root_keyword         PRIMARY KEY (root_id, keyword_id),
    CONSTRAINT fk_root_keyword_root    FOREIGN KEY (root_id)    REFERENCES root(id)    ON DELETE CASCADE,
    CONSTRAINT fk_root_keyword_keyword FOREIGN KEY (keyword_id) REFERENCES keyword(id) ON DELETE CASCADE
);

CREATE TABLE root_image (
    root_id    BIGINT   NOT NULL,
    image_id   BIGINT   NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

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
    CONSTRAINT fk_store_root         FOREIGN KEY (id)         REFERENCES root(id) ON DELETE CASCADE,
    CONSTRAINT fk_store_address      FOREIGN KEY (address_id) REFERENCES address(id),
    CONSTRAINT uk_store_address_name UNIQUE      (address_id, name)
);

CREATE TABLE category (
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    parent_id  BIGINT,
    name       VARCHAR(50) NOT NULL,
    created_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_category             PRIMARY KEY (id),
    CONSTRAINT fk_category_parent      FOREIGN KEY (parent_id) REFERENCES category(id) ON DELETE SET NULL,
    CONSTRAINT uk_category_parent_name UNIQUE      (parent_id, name)
);

CREATE TABLE store_category (
    store_id    BIGINT   NOT NULL,
    category_id BIGINT   NOT NULL,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_store_category          PRIMARY KEY (store_id, category_id),
    CONSTRAINT fk_store_category_store    FOREIGN KEY (store_id)    REFERENCES store(id)    ON DELETE CASCADE,
    CONSTRAINT fk_store_category_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE
);

CREATE TABLE facility_category (
    id         BIGINT      NOT NULL AUTO_INCREMENT,
    name       VARCHAR(50) NOT NULL,
    created_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_facility_category      PRIMARY KEY (id),
    CONSTRAINT uk_facility_category_name UNIQUE      (name)
);

CREATE TABLE store_facility_category (
    store_id             BIGINT   NOT NULL,
    facility_category_id BIGINT   NOT NULL,
    created_at           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at           DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

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
    CONSTRAINT fk_food_root       FOREIGN KEY (id)       REFERENCES root(id)  ON DELETE CASCADE,
    CONSTRAINT fk_food_store      FOREIGN KEY (store_id) REFERENCES store(id) ON DELETE CASCADE,
    CONSTRAINT uk_food_store_name UNIQUE      (store_id, name),
    CONSTRAINT ck_food_price      CHECK       (price >= 0)
);

-- 유저
CREATE TABLE user (
    id             BIGINT         NOT NULL,
    role_id        BIGINT         NOT NULL,
    email          VARCHAR(100)   NOT NULL,
    password       VARCHAR(256),
    nickname       VARCHAR(20)    NOT NULL,
    description    TEXT,
    gender         ENUM('M', 'F') NOT NULL,
    year           INT            NOT NULL,
    email_verified BOOLEAN        NOT NULL DEFAULT FALSE,
    show_reviews   BOOLEAN        NOT NULL DEFAULT FALSE,
    show_likes     BOOLEAN        NOT NULL DEFAULT FALSE,
    show_bookmarks BOOLEAN        NOT NULL DEFAULT FALSE,
    show_following BOOLEAN        NOT NULL DEFAULT FALSE,
    show_followers BOOLEAN        NOT NULL DEFAULT FALSE,
    last_login_at  DATETIME,

    CONSTRAINT pk_user          PRIMARY KEY (id),
    CONSTRAINT fk_user_root     FOREIGN KEY (id)      REFERENCES root(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_role     FOREIGN KEY (role_id) REFERENCES role(id),
    CONSTRAINT uk_user_email    UNIQUE      (email),
    CONSTRAINT uk_user_nickname UNIQUE      (nickname)
);

CREATE TABLE social_account (
    user_id          BIGINT         NOT NULL,
    provider         ENUM('GOOGLE') NOT NULL,
    provider_user_id VARCHAR(191)   NOT NULL,
    provider_email   VARCHAR(191),
    email_verified   BOOLEAN        NOT NULL DEFAULT FALSE,
    display_name     VARCHAR(100),
    avatar_url       VARCHAR(256),
    created_at       DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login_at    DATETIME,

    CONSTRAINT pk_social_account                           PRIMARY KEY (user_id, provider),
    CONSTRAINT fk_social_account_user                      FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT uk_social_account_provider_provider_user_id UNIQUE      (provider, provider_user_id)
);

CREATE TABLE bookmark (
    user_id    BIGINT   NOT NULL,
    store_id   BIGINT   NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_bookmark       PRIMARY KEY (user_id, store_id),
    CONSTRAINT fk_bookmark_user  FOREIGN KEY (user_id)  REFERENCES user(id)  ON DELETE CASCADE,
    CONSTRAINT fk_bookmark_store FOREIGN KEY (store_id) REFERENCES store(id) ON DELETE CASCADE
);

CREATE TABLE review (
    id               BIGINT  NOT NULL,
    user_id          BIGINT  NOT NULL,
    store_id         BIGINT  NOT NULL,
    receipt_image_id BIGINT,
    receipt_status   BOOLEAN NOT NULL DEFAULT FALSE,
    rating           INT     NOT NULL,
    content          TEXT,

    CONSTRAINT pk_review               PRIMARY KEY (id),
    CONSTRAINT fk_review_root          FOREIGN KEY (id)               REFERENCES root(id)  ON DELETE CASCADE,
    CONSTRAINT fk_review_user          FOREIGN KEY (user_id)          REFERENCES user(id)  ON DELETE CASCADE,
    CONSTRAINT fk_review_store         FOREIGN KEY (store_id)         REFERENCES store(id) ON DELETE CASCADE,
    CONSTRAINT fk_review_receipt_image FOREIGN KEY (receipt_image_id) REFERENCES image(id) ON DELETE SET NULL,
    CONSTRAINT ck_review_rating        CHECK       (rating            BETWEEN 1 AND 5)
);

CREATE TABLE review_food (
    review_id BIGINT NOT NULL,
    food_id BIGINT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_review_food PRIMARY KEY (review_id, food_id),
    CONSTRAINT fk_review_food_review FOREIGN KEY (review_id) REFERENCES review(id) ON DELETE CASCADE,
    CONSTRAINT fk_review_food_food FOREIGN KEY (food_id) REFERENCES food(id) ON DELETE CASCADE
);

CREATE TABLE coeat (
    id          BIGINT                              NOT NULL,
    store_id    BIGINT,
    user_id     BIGINT                              NOT NULL,
    title       VARCHAR(100)                        NOT NULL,
    content     TEXT                                NOT NULL,
    capacity    INT                                 NOT NULL,
    view_count  INT                                 NOT NULL DEFAULT 0,
    meeting_at  DATETIME                            NOT NULL,
    status      ENUM('OPEN', 'CLOSED', 'CANCELLED') NOT NULL DEFAULT 'OPEN',
    auto_accept BOOLEAN                             NOT NULL DEFAULT FALSE,

    CONSTRAINT pk_coeat          PRIMARY KEY (id),
    CONSTRAINT fk_coeat_root     FOREIGN KEY (id)       REFERENCES root(id)  ON DELETE CASCADE,
    CONSTRAINT fk_coeat_store    FOREIGN KEY (store_id) REFERENCES store(id) ON DELETE SET NULL,
    CONSTRAINT fk_coeat_user     FOREIGN KEY (user_id)  REFERENCES user(id)  ON DELETE CASCADE,
    CONSTRAINT ck_coeat_capacity CHECK       (capacity >= 1)
);

CREATE TABLE coeat_request (
    coeat_id   BIGINT                                               NOT NULL,
    user_id    BIGINT                                               NOT NULL,
    status     ENUM('PENDING', 'APPROVED', 'REJECTED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    message    TEXT,
    -- reject_reason VARCHAR(200) -- 거절 사유
    created_at DATETIME                                             NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME                                             NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_coeat_request       PRIMARY KEY (coeat_id, user_id),
    CONSTRAINT fk_coeat_request_coeat FOREIGN KEY (coeat_id) REFERENCES coeat(id) ON DELETE CASCADE,
    CONSTRAINT fk_coeat_request_user  FOREIGN KEY (user_id)  REFERENCES user(id)  ON DELETE CASCADE
);

CREATE TABLE comment (
    id        BIGINT NOT NULL,
    coeat_id  BIGINT NOT NULL,
    parent_id BIGINT,
    user_id   BIGINT NOT NULL,
    content   TEXT   NOT NULL,

    CONSTRAINT pk_comment        PRIMARY KEY (id),
    CONSTRAINT fk_comment_root   FOREIGN KEY (id)        REFERENCES root(id)    ON DELETE CASCADE,
    CONSTRAINT fk_comment_user   FOREIGN KEY (user_id)   REFERENCES user(id)    ON DELETE CASCADE,
    CONSTRAINT fk_comment_coeat  FOREIGN KEY (coeat_id)  REFERENCES coeat(id)   ON DELETE CASCADE,
    CONSTRAINT fk_comment_parent FOREIGN KEY (parent_id) REFERENCES comment(id) ON DELETE SET NULL
);

CREATE TABLE report (
    id          BIGINT                                                            NOT NULL AUTO_INCREMENT,
    reporter_id BIGINT                                                            NOT NULL,
    root_id     BIGINT                                                            NOT NULL,
    reason      TEXT                                                              NOT NULL,
    status      ENUM('PENDING', 'IN_REVIEW', 'RESOLVED', 'REJECTED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
    created_at  DATETIME                                                          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME                                                          NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_report               PRIMARY KEY (id),
    CONSTRAINT fk_report_reporter      FOREIGN KEY (reporter_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_report_root          FOREIGN KEY (root_id)     REFERENCES root(id) ON DELETE CASCADE,
    CONSTRAINT uk_report_reporter_root UNIQUE      (reporter_id, root_id)
);

CREATE TABLE block (
    blocker_id BIGINT   NOT NULL,
    blockee_id BIGINT   NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_block         PRIMARY KEY (blocker_id, blockee_id),
    CONSTRAINT fk_block_blocker FOREIGN KEY (blocker_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_block_blockee FOREIGN KEY (blockee_id) REFERENCES user(id) ON DELETE CASCADE
);

CREATE TABLE follow (
    follower_id BIGINT   NOT NULL,
    followee_id BIGINT   NOT NULL,
    notified    BOOLEAN  NOT NULL DEFAULT FALSE,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_follow          PRIMARY KEY (follower_id, followee_id),
    CONSTRAINT fk_follow_follower FOREIGN KEY (follower_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_follow_followee FOREIGN KEY (followee_id) REFERENCES user(id) ON DELETE CASCADE
);

CREATE TABLE likes (
    user_id    BIGINT   NOT NULL,
    root_id    BIGINT   NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_likes      PRIMARY KEY (user_id, root_id),
    CONSTRAINT fk_likes_user FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_likes_root FOREIGN KEY (root_id) REFERENCES root(id) ON DELETE CASCADE
);

CREATE TABLE notification (
    id             BIGINT   NOT NULL AUTO_INCREMENT,
    actor_user_id  BIGINT   NOT NULL,
    root_id        BIGINT   NOT NULL,
    target_user_id BIGINT   NOT NULL,
    message        TEXT     NOT NULL,
    read_at        DATETIME,
    created_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT pk_notification             PRIMARY KEY (id),
    CONSTRAINT fk_notification_actor_user  FOREIGN KEY (actor_user_id)  REFERENCES user(id) ON DELETE CASCADE,
    CONSTRAINT fk_notification_root        FOREIGN KEY (root_id)        REFERENCES root(id) ON DELETE CASCADE,
    CONSTRAINT fk_notification_target_user FOREIGN KEY (target_user_id) REFERENCES user(id) ON DELETE CASCADE
);

CREATE TABLE api_key (
    id          INT           NOT NULL AUTO_INCREMENT COMMENT 'Primary Key',
    `key`       VARCHAR(1000) NOT NULL,
    name        VARCHAR(255)  NOT NULL,
    owner       VARCHAR(255)  NOT NULL,
    description VARCHAR(255),
    created_at   DATETIME      DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT pk_api_key PRIMARY KEY (id)
) COMMENT 'api key 보관용 테이블 입니다.';


/* =========================
   주소 / 지역
   ========================= */

-- 읍면동 단위 주소 조회/집계
CREATE INDEX idx_address_emd_id ON address (emd_id);

-- 주소를 도로+우편번호로 정확히 조회(=,= 조건 최적화)
-- CREATE INDEX idx_address_road_postal ON address (road_id, postal_id);

-- 우편번호 단일 필터/집계 최적화
-- CREATE INDEX idx_address_postal_id ON address (postal_id);

-- 우편번호에서 연결 도로 역방향 탐색 최적화
-- CREATE INDEX idx_road_postal_postal_id ON road_postal (postal_id, road_id);


/* =========================
   위경도 (사각 범위 검색)
   ========================= */

-- 사각 범위(BBOX) 검색용 후보군 축소 인덱스
CREATE INDEX idx_address_lat_lon ON address (latitude, longitude);


/* =========================
   루트(슈퍼타입)
   ========================= */

-- 타입별 최신/범위 조회 최적화 (type 선행 + id 정렬/범위)
CREATE INDEX idx_root_type_id ON root (type, id);


/* =========================
   매핑 테이블 (역방향 조회 보조)
   ========================= */

-- 키워드 -> 루트 역방향 탐색 최적화
CREATE INDEX idx_root_keyword_keyword_id ON root_keyword (keyword_id);

-- 이미지 -> 루트 역방향 탐색 최적화
CREATE INDEX idx_root_image_image_id ON root_image (image_id);


/* =========================
   상점 / 카테고리 / 시설
   ========================= */

-- 주소별 상점 목록/집계
CREATE INDEX idx_store_address_id ON store (address_id);

-- 부모 카테고리 하위 목록 조회
CREATE INDEX idx_category_parent_id ON category (parent_id);

-- 카테고리 -> 상점 역방향 탐색
CREATE INDEX idx_store_category_category_id ON store_category (category_id);

-- 시설카테고리 -> 상점 역방향 탐색
CREATE INDEX idx_store_facility_category_id ON store_facility_category (facility_category_id);


/* =========================
   음식
   ========================= */

-- 상점의 메뉴 전체 조회
CREATE INDEX idx_food_store_id ON food (store_id);

-- 상점 내 가격대 필터/정렬 (store_id 선행 + price 범위/정렬)
CREATE INDEX idx_food_store_price ON food (store_id, price);


/* =========================
   유저 / 소셜
   ========================= */

-- 역할/권한별 사용자 조회/집계
CREATE INDEX idx_user_role_id ON user (role_id);


/* =========================
   북마크 / 팔로우 / 차단 / 좋아요
   ========================= */

-- 가게의 북마크 수/목록 (역방향)
CREATE INDEX idx_bookmark_store_user ON bookmark (store_id, user_id);

-- 나를 팔로우하는 사용자 목록 (역방향)
CREATE INDEX idx_follow_followee_id ON follow (followee_id);

-- 나를 차단한 사용자 목록 (역방향)
CREATE INDEX idx_block_blockee_id ON block (blockee_id);

-- 컨텐츠의 좋아요 수/목록 (루트 기준 역방향)
CREATE INDEX idx_likes_root_id ON likes (root_id);

-- 사용자가 누른 좋아요 목록 (마이페이지 등)
CREATE INDEX idx_likes_user_id ON likes (user_id);


/* =========================
   리뷰
   ========================= */

-- 가게별 리뷰 조회
CREATE INDEX idx_review_store_id ON review (store_id);

-- 사용자별 리뷰 조회
CREATE INDEX idx_review_user_id ON review (user_id);

-- 메뉴(음식)별 리뷰 역방향 조회
CREATE INDEX idx_review_food_food ON review_food (food_id);

-- 가게 리뷰 최신순 페이징 (store_id 선행 + id DESC)
CREATE INDEX idx_review_store_created ON review (store_id, id DESC);


/* =========================
   코잇(모임)
   ========================= */

-- 사용자가 만든 모임 목록
CREATE INDEX idx_coeat_user_id ON coeat (user_id);

-- 상점별 모임 목록
CREATE INDEX idx_coeat_store_id ON coeat (store_id);

-- 모집중&임박 리스트 (status 선행 + 시간 범위/정렬)
CREATE INDEX idx_coeat_status_meeting_at ON coeat (status, meeting_at);


/* =========================
   코잇 신청
   ========================= */

-- 사용자 신청 내역을 상태로 필터
CREATE INDEX idx_coeat_request_user_status ON coeat_request (user_id, status);

-- 모임 단위 신청자 목록(대기/승인 등)
CREATE INDEX idx_coeat_request_coeat_status ON coeat_request (coeat_id, status);


/* =========================
   댓글 (코잇 전용 모델)
   ========================= */

-- 특정 모임의 대댓글 트리 조회 (coeat_id + parent_id)
CREATE INDEX idx_comment_coeat_parent ON comment (coeat_id, parent_id);

-- 특정 모임 댓글의 시간/ID 순 페이징
CREATE INDEX idx_comment_coeat_id ON comment (coeat_id, id);

-- 사용자별 댓글 활동
CREATE INDEX idx_comment_user_id ON comment (user_id);

-- 한 부모 아래 자식 댓글들 조회
CREATE INDEX idx_comment_parent_id ON comment (parent_id);


/* =========================
   신고
   ========================= */

-- 사용자가 올린 신고 내역
CREATE INDEX idx_report_reporter_id ON report (reporter_id);

-- 특정 컨텐츠에 들어온 신고 목록
CREATE INDEX idx_report_root_id ON report (root_id);

-- 상태별 업무 큐(관리화면)
CREATE INDEX idx_report_status ON report (status);


/* =========================
   알림
   ========================= */

-- 미읽음 알림 빠른 조회/읽음 처리 (target_user_id + read_at NULL)
CREATE INDEX idx_notification_target_read ON notification (target_user_id, read_at);

-- 특정 사용자가 발생시킨 활동 기반 알림
CREATE INDEX idx_notification_actor ON notification (actor_user_id);

-- 특정 컨텐츠 관련 알림
CREATE INDEX idx_notification_root ON notification (root_id);