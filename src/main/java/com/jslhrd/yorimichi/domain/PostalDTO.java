package com.jslhrd.yorimichi.domain;

import lombok.*;

@Getter
@Setter
public class PostalDTO {
    private Long postalId;
    private Long roadId;
    private String code; // 우편번호 (CHAR(5)) - PK   
}
