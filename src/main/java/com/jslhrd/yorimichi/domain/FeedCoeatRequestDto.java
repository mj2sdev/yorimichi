package com.jslhrd.yorimichi.domain;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class FeedCoeatRequestDto {
    private Long feedId;          // FK
    private Long userId;          // FK
    private String status;
    private String message;
    private LocalDateTime createdAt;
}
