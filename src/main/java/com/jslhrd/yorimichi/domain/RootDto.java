package com.jslhrd.yorimichi.domain;

import lombok.*;
import java.time.LocalDateTime;

public class RootDto {
    private Long rootId;                // BIGINT, PK (AUTO_INCREMENT)
    private LocalDateTime createdAt;    // DATETIME, DEFAULT CURRENT_TIMESTAMP
    private LocalDateTime updatedAt;    // DATETIME, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    private LocalDateTime deletedAt;    // DATETIME, NULL (삭제일시)
    private LocalDateTime blindedAt;    // DATETIME, NULL (검열일시)
}