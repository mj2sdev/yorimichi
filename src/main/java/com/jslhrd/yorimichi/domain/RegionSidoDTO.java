package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 시/도
 */
@Getter
@Setter
public class RegionSidoDTO {
    private Long id;     // PK
    private String code; // 시/도 코드
    private String name; // 시/도 명칭
}
