package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 음식
 * - ERD: id, store_id, name, price, description
 * - created_at 없음
 */
@Getter
@Setter
public class FoodDTO {
    private Long id;          // PK
    private Long storeId;     // FK → store.id
    private String name;      // 음식 이름
    private Integer price;    // 가격
    private String description; // 설명 (NULL 허용)
}
