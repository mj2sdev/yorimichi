package com.jslhrd.yorimichi.domain;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class CategoryDTO {
    private Long id;        // PK erd 기준 categoryId -> id 로 수정
    private Long parentId;  // 부모 카테고리 FK
    private String name;            // 종류명 (VARCHAR(20))
    private LocalDateTime createdAt;
}
