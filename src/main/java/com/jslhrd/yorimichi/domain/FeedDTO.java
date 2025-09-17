package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 피드(게시물)
 * - 최신 ERD 기준: id, user_id, type, title, content, view_count
 * - created_at 컬럼 없음
 */
@Getter
@Setter
public class FeedDTO {
    private Long id;            // PK
    private Long userId;        // FK → user.id
    private String type;        // 피드 타입(예: NORMAL/COEAT 등)
    private String title;       // 제목
    private String content;     // 본문
    private Integer viewCount;  // 조회수
}
