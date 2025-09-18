package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 같이먹기(모집) 정보
 *  id 삭제 (2차 수정)
 */
@Getter
@Setter
public class FeedCoeatDTO {
    private Long feedId;                // FK → feed.id
    private Long storeId;               // FK → store.id
    private Integer capacity;           // 정원
    private LocalDateTime meetingAt;    // 모임 시각
    private Boolean autoAccept;         // 자동 수락 여부
    private String status;              // 상태(예: OPEN/CLOSED 등)
}
