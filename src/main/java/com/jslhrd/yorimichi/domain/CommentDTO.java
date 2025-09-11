package com.jslhrd.yorimichi.domain;

import lombok.*;
@Getter
@Setter
public class CommentDTO {
    private Long commentId;  
    private Long userId;
    private Long feedId;
    private Long parentId;   // FIX: parentCommentId → parentId
    private String content;
}
