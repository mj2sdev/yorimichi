package com.jslhrd.yorimichi.domain;

import lombok.*;

@Getter
@Setter
public class BookmarkDTO {
    private Long userId;   // FK → user.user_id
    private Long storeId;  // FK → store.store_id
}
