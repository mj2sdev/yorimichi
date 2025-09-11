package com.jslhrd.yorimichi.domain;


import lombok.*;

@Getter
@Setter
public class FoodCategory {
    private Long foodId;      // FK → food.food_id
    private Long categoryId;  // FK → category.category_id    
}
