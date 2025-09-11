package com.jslhrd.yorimichi.domain;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationDTO {
    private Long notificationId;  // PK
    private Long rootId;          // FK → root.root_id
    private Long userId;          // FK → user.user_id
    private String type;          // DB ENUM (필요 시 자바 enum으로 교체)
    private String message;       // NULL 가능
    private Boolean isRead;       // 기본 FALSE
    private LocalDateTime createdAt; // CURRENT_TIMESTAMP
}
