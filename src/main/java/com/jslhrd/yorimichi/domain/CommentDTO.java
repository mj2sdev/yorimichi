package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 댓글
 * - parent_id: 대댓글(자기참조), 최상위 댓글은 NULL
 * - ERD 기준: created_at 없음
 */
@Getter
@Setter
public class CommentDTO {
    private Long id;        // PK
    private Long userId;    // FK → user.id
    private Long feedId;    // FK → feed.id
    private Long parentId;  // 상위 댓글(자기참조), NULL 허용
    private String content; // 내용
}
