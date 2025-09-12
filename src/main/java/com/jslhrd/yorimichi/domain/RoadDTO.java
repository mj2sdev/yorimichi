package com.jslhrd.yorimichi.domain;

import lombok.*;

@Getter
@Setter
public class RoadDTO {
    private Long id;
    private Long emdId;
    private String code;        // 도로명 코드 (VARCHAR(12)) - PK
    private String name;        // 도로명 이름 (VARCHAR(100))
}
