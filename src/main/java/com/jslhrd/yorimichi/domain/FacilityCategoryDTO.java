package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 시설 카테고리
 */
@Getter
@Setter
public class FacilityCategoryDTO {
    private Long id;                 // PK
    private String name;             // 명칭
    private LocalDateTime createdAt; // 생성시각 (DB DEFAULT)
}
