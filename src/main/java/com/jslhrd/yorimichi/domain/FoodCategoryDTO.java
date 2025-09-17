package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 음식-카테고리 매핑
 * - (food_id, category_id) 복합키 성격
 */
@Getter
@Setter
public class FoodCategoryDTO {
    private Long foodId;               // FK → food.id
    private Long categoryId;           // FK → category.id
    private LocalDateTime createdAt;   // 생성시각 (DB DEFAULT)
}
