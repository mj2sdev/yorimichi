package com.jslhrd.yorimichi.domain;

import lombok.*;

@Getter
@Setter
public class RegionSidoDTO {
    private Long id;
    private String code; // 시/도 코드 (CHAR(2)) - PK
    private String name; // 시/도 이름
}
