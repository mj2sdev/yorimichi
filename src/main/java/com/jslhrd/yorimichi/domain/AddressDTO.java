package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

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
@Alias("AddressDTO")
public class AddressDTO {

    /** PK: 주소 ID */
    private Long id;

    /** FK: 도로 ID */
    private Long roadId;

    /** FK: 우편번호 ID */
    private Long postalId;

    /** 상세주소(동/호수 등) */
    private String detail;

    /** 도로명 전체 주소 */
    private String roadAddressText;

    /** 지번 전체 주소 */
    private String jibunAddressText;

    /** 위도 (-90.0 ~ +90.0) */
    private Double latitude;

    /** 경도 (-180.0 ~ +180.0) */
    private Double longitude;

    /** 생성일시 (DB 자동 생성) */
    private LocalDateTime createdAt;

    /** 수정일시 (DB 자동 갱신) */
    private LocalDateTime updatedAt;
}