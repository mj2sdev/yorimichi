package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 같이먹기(모집) 신청
 * - (feed_id, user_id) 조합으로 단일 신청 구분 가능
 */
@Getter
@Setter
public class FeedCoeatRequestDTO {
    private Long feedId;               // FK → feed.id
    private Long userId;               // FK → user.id
    private String status;             // 상태(예: PENDING/APPROVED/REJECTED)
    private String message;            // 신청 메시지
    private LocalDateTime createdAt;   // 생성시각 (DB DEFAULT)
}
