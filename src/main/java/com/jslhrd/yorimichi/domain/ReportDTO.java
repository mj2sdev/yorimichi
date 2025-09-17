package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 신고
 * - reporter_id: 신고한 유저
 * - reported_id: 신고 대상 유저
 * - created_at: DB DEFAULT
 * - updated_at: 수정 시각
 */
@Getter
@Setter
public class ReportDTO {
    private Long reporterId;           // FK → user.id
    private Long reportedId;           // FK → user.id
    private String description;        // 신고 사유
    private String status;             // 상태(예: OPEN/RESOLVED 등)
    private LocalDateTime createdAt;   // 생성시각 (DB DEFAULT)
    private LocalDateTime updatedAt;   // 수정시각
}
