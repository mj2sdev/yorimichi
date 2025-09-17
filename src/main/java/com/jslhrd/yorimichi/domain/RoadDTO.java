package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 도로
 * - FK: emd_id → region_emd.id
 */
@Getter
@Setter
public class RoadDTO {
    private Long id;       // PK
    private Long emdId;    // FK → region_emd.id
    private String code;   // 도로 코드
    private String name;   // 도로 명칭
}
