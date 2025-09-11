package com.jslhrd.yorimichi.domain;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class AddressDTO {
    private Long addressId;
    private String regionSidoCode;     // 시/도 코드 (CHAR(2))
    private String regionSigunguCode;  // 시/군/구 코드 (CHAR(5))
    private String regionEmdCode;      // 읍/면/동 코드 (CHAR(8))
    private String roadCode;           // 도로명 코드 (VARCHAR(12))
    private String postalCode;         // 우편번호 (CHAR(5))
    private String detail;             // 상세 주소 (동/호수 등)
    private String roadAddressText;    // 입력 당시 도로명 주소
    private String jibunAddressText;   // 입력 당시 지번 주소
    private Double latitude;           // 위도 DECIMAL(10,7)
    private Double longitude;          // 경도 DECIMAL(10,7)
    private LocalDateTime createdAt;   // 생성일시
    private LocalDateTime updatedAt;   // 수정일시
}
