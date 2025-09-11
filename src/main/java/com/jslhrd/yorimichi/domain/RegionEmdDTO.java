package com.jslhrd.yorimichi.domain;

import lombok.*;

@Getter
@Setter
public class RegionEmdDTO {
    private String code;        // 읍/면/동 코드 (CHAR(8)) - PK
    private String name;        // 읍/면/동 이름 (VARCHAR(30))
    private String sigunguCode; // 시/군/구 FK (CHAR(5)) -> region_sigungu.code
}
