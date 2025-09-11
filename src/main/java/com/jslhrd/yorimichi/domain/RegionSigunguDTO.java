package com.jslhrd.yorimichi.domain;

import lombok.*;

@Getter
@Setter
public class RegionSigunguDTO {
    private String code;     // 시/군/구 코드 (CHAR(5)) - PK
    private String name;     // 시/군/구 이름
    private String sidoCode; // 시/도 FK (CHAR(2)) -> region_sido.code   
}
