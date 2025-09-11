package com.jslhrd.yorimichi.domain;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
public class StoreFacilityCategoryDTO {
    private Long storeId;            // FK → store.store_id
    private Long facilityCategoryId; // FK → facility_category.facility_category_id
    private LocalDateTime createdAt;
}
