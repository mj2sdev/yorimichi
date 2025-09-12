package com.jslhrd.yorimichi.domain;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class FacilityCategoryDTO {
    private Long facilityCategoryId; // PK
    private String name;             // 시설 카테고리명
    private LocalDateTime createdAt;
}
