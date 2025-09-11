package com.jslhrd.yorimichi.domain;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
public class AddressDTO {
    private Long addressId;        // PK (ERD: id) - 매퍼에서 alias 필요
    private Long roadId;           // FK -> road.id
    private String detail;
    private String roadAddressText;
    private String jibunAddressText;
    private Double latitude;
    private Double longitude;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
