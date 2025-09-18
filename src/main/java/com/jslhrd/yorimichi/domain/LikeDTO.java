package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 좋아요
 * - ERD: id, user_id, root_id, created_at
 * - created_at: DB DEFAULT
 *  schema 기준 id 제거 (2차 수정)
 */
@Getter
@Setter
public class LikeDTO {
    private Long userId;             // 유저 FK
    private Long rootId;             // 루트 FK
    private LocalDateTime createdAt; // 생성시각 (DB DEFAULT)
}
