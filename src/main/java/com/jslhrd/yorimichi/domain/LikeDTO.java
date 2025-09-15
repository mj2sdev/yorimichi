package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 좋아요
 * - ERD: id, user_id, root_id, created_at
 * - created_at: DB DEFAULT
 */
@Getter
@Setter
public class LikeDTO {
    private Long id;                 // PK
    private Long userId;             // 유저 FK
    private Long rootId;             // 루트 FK
    private LocalDateTime createdAt; // 생성시각 (DB DEFAULT)
}
