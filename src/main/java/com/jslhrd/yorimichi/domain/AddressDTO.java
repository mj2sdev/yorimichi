package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import lombok.*;

/**
 * 주소 DTO.
 *
 * <br>주소 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
@ToString(callSuper = true)
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
