package com.jslhrd.yorimichi.domain;

import lombok.*;

@Getter
@Setter
public class RoadDTO {
    private String code;        // 도로명 코드 (VARCHAR(12)) - PK
    private String name;        // 도로명 이름 (VARCHAR(100))
    private String sigunguCode; // 시/군/구 FK (CHAR(5)) -> region_sigungu.code
}
