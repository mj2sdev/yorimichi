package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 차단 관계
 * - (blocker_id, blockee_id) 복합키 성격
 */
@Getter
@Setter
public class BlockDTO {
    private Long blockerId;             // 차단한 유저 FK
    private Long blockeeId;             // 차단당한 유저 FK
    private LocalDateTime createdAt;    // 생성시각 (DB DEFAULT)
}
