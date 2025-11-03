package com.jslhrd.yorimichi.domain;

import java.math.BigDecimal;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AddressDTO {
    private Long id;

    // FK들 (PR 방향에 맞춰 road/postal로 분리)
    private Long roadId;
    private Long postalId;

    // 주소 텍스트
    private String detail;
    private String roadAddressText;
    private String jibunAddressText;

    // 좌표
    private BigDecimal latitude;
    private BigDecimal longitude;

}
