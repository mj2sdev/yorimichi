package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 알림
 * - actorUserId : 액션을 발생시킨 유저
 * - targetUserId: 알림을 받는 유저
 * - readAt      : 읽은 시각 (NULL = 미읽음)
 */
@Getter
@Setter
public class NotificationDTO {
    private Long id;                  // PK
    private Long actorUserId;         // FK → user.id (행위자)
    private Long rootId;              // FK → root.id (대상 루트)
    private Long targetUserId;        // FK → user.id (수신자)
    private String message;           // 내용
    private LocalDateTime createdAt;  // 생성시각 (DB DEFAULT)
    private LocalDateTime readAt;     // 읽은 시각 (NULL 가능)
    private LocalDateTime updatedAt;  // 수정시각
}
