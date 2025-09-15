package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 상점-시설카테고리 매핑
 * - (store_id, facility_category_id) 복합키 성격
 */
@Getter
@Setter
public class StoreFacilityCategoryDTO {
    private Long storeId;                 // FK → store.id
    private Long facilityCategoryId;      // FK → facility_category.id
    private LocalDateTime createdAt;      // 생성시각 (DB DEFAULT)
}
