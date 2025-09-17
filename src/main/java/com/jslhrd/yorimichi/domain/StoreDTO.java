package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 상점
 */
@Getter
@Setter
public class StoreDTO {
    private Long id;            // PK
    private Long addressId;     // FK → address.id
    private String name;        // 상점 이름
    private String description; // 설명 (NULL 허용)
    private String phone;       // 전화번호 (NULL 허용)
}
