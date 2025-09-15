package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 우편번호
 * - roadId: 도로 FK
 * - code  : 우편번호 문자열
 */
@Getter
@Setter
public class PostalDTO {
    private Long id;      // PK
    private Long roadId;  // FK → road.id
    private String code;  // 우편번호
}
