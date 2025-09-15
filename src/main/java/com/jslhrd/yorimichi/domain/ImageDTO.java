package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 이미지
 * - url: 저장 경로/주소
 * - created_at: DB DEFAULT
 */
@Getter
@Setter
public class ImageDTO {
    private Long id;                 // PK
    private String url;              // 이미지 URL/경로
    private LocalDateTime createdAt; // 생성시각 (DB DEFAULT)
}
