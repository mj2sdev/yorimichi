package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 상점 북마크
 * - (user_id, store_id) 복합키 성격
 */
@Getter
@Setter
public class BookmarkDTO {
    private Long userId;                // 유저 FK
    private Long storeId;               // 상점 FK
    private LocalDateTime createdAt;    // 생성시각 (DB DEFAULT)
}
