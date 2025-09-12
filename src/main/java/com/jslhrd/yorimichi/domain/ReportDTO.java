package com.jslhrd.yorimichi.domain;


import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class ReportDTO {
    private Long reporterId;            // FK → user.user_id (신고한 유저)
    private Long reportedId;            // FK → user.user_id (신고당한 유저)
    private String description;         // TEXT
    private String status;
    private LocalDateTime createdAt;    // CURRENT_TIMESTAMP
    private LocalDateTime updatedAt;    // CURRENT_TIMESTAMP ON UPDATE
}
