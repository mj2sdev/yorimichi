package com.jslhrd.yorimichi.domain;

import lombok.*;

@Getter
@Setter
public class CategoryDTO {
    private Long categoryId;        // PK
    private Long parentCategoryId;  // 부모 카테고리 FK
    private String name;            // 종류명 (VARCHAR(20))
}
