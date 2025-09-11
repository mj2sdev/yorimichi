package com.jslhrd.yorimichi.domain;

import lombok.*;

@Getter
@Setter
public class StoreCategoryDTO {
    private Long storeId;     // FK → store.store_id
    private Long categoryId;  // FK → category.category_id    
}
