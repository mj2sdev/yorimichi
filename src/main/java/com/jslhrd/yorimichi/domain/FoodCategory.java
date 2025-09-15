package com.jslhrd.yorimichi.domain;


import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class FoodCategory {
    private Long foodId;      // FK → food.food_id
    private Long categoryId;  // FK → category.category_id
    private LocalDateTime createdAt;    
}
