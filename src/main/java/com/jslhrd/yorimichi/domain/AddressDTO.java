package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 주소
 * - FK: road_id
 * - created_at: DB DEFAULT, update에서만 updated_at 갱신
 */
@Getter
@Setter
public class AddressDTO {
    private Long id;                    // PK
    private Long roadId;                // FK → road.id
    private Long postalId;              // postalId 추가(2차수정)
    private String detail;              // 상세 주소(동/호수 등)
    private String roadAddressText;     // 도로명 전체 주소
    private String jibunAddressText;    // 지번 전체 주소
    private Double latitude;        // 위도  Bigdecimal -> double 변경 (2차수정)
    private Double longitude;       // 경도  Bigdecimal -> double 변경 (2차수정)
    private LocalDateTime createdAt;    // 생성시각 (DB DEFAULT)
    private LocalDateTime updatedAt;    // 수정시각
}
