package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 시/군/구
 * - FK: sido_id → region_sido.id
 */
@Getter
@Setter
public class RegionSigunguDTO {
    private Long id;       // PK
    private Long sidoId;   // FK → region_sido.id
    private String code;   // 시군구 코드
    private String name;   // 시군구 명칭
}
