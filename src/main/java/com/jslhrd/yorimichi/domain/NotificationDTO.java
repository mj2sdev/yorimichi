package com.jslhrd.yorimichi.domain;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationDTO {
    private Long notificationId;  // PK
    private Long actorUserId;
    private Long rootId;          // FK → root.root_id
    private Long targetUserId;
    private String message;       // NULL 가능
    private LocalDateTime readAt;     // NEW (isRead 대체)
    private LocalDateTime updatedAt;  // NEW
    private LocalDateTime createdAt; // CURRENT_TIMESTAMP
}
