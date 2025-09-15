package com.jslhrd.yorimichi.domain;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class LikeDTO {
    private Long likeId;           // PK
    private Long userId;           // FK → user.user_id
    private Long rootId;           // FK → root.root_id
    private LocalDateTime createdAt; // 생성일시
}
