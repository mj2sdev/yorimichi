package com.jslhrd.yorimichi.domain;


import lombok.*;

@Getter
@Setter
public class FoodCategoryDTO {
    private Long foodId;      // FK → food.food_id
    private Long categoryId;  // FK → category.category_id    
}
