package com.jslhrd.yorimichi.domain;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class ImageDTO {
    /** 이미지 PK (BIGINT AUTO_INCREMENT) */
    private Long imageId;
    /** 이미지 URL (VARCHAR, NOT NULL) */
    private String url;
    /** 생성일시 (DATETIME, DEFAULT CURRENT_TIMESTAMP) */
    private LocalDateTime createdAt;
}
