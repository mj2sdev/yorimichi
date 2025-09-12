package com.jslhrd.yorimichi.domain;

import lombok.*;

@Getter
@Setter
public class RegionEmdDTO {
    private Long emdId;
    private Long sigubguId;
    private String code;        // 읍/면/동 코드 (CHAR(8)) - PK
    private String name;        // 읍/면/동 이름 (VARCHAR(30))
}
