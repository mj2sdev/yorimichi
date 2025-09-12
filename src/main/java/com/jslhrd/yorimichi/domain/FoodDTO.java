package com.jslhrd.yorimichi.domain;

import lombok.*;

@Getter
@Setter
public class FoodDTO {
    private Long foodId;
    private Long storeId;
    private String name;
    private Integer price;
    private String description; // NULL 가능    
}
