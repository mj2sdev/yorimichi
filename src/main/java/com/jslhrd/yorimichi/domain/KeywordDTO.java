package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 키워드
 * - created_at: DB DEFAULT
 */
@Getter
@Setter
public class KeywordDTO {
    private Long id;                 // PK
    private String name;             // 키워드 명
    private LocalDateTime createdAt; // 생성시각 (DB DEFAULT)
}
