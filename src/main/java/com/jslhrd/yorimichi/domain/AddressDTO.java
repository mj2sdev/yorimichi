package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 주소 DTO.
 *
 * <br>
 * 주소 정보를 전달합니다.
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
	private Long emdId;

	/**
	 * FK: 도로 ID
	 */
	// private Long roadId;

	/**
	 * FK: 우편번호 ID
	 */
	// private Long postalId;

	/**
	 * 상세주소(동/호수 등)
	 */
	private String detail;
	/**
	 * 도로명 전체 주소
	 */
	private String roadAddressText;
	/**
	 * 지번 전체 주소
	 */
	private String jibunAddressText;

	/**
	 * FK: 도로 ID
	 */
//	private Long roadId;

	/**
	 * FK: 우편번호 ID
	 */
//	private Long postalId;
	private String placeId;
	/**
	 * 생성일시 (DB 자동 생성)
	 */
	private LocalDateTime createdAt;
	/**
	 * 위도 (-90.0 ~ +90.0)
	 */
//	private Double latitude;
	/**
	 * 경도 (-180.0 ~ +180.0)
	 */
//	private Double longitude;
	/**
	 * 수정일시 (DB 자동 갱신)
	 */
	private LocalDateTime updatedAt;

	/**
	 * 주소의 상점
	 */
	private StoreDTO store;
	private RegionEmdDTO emd;
	
	public AddressDTO() {
	}

	public AddressDTO(Long emdId, String detail, String roadAddressText, String jibunAddressText, String placeId) {
		this.emdId = emdId;
		this.detail = detail;
		this.roadAddressText = roadAddressText;
		this.jibunAddressText = jibunAddressText;
		this.placeId = placeId;
	}

	/**
	 * 주소의 우편번호
	 */
	// private PostalDTO postal;

	/**
	 * 주소의 도로명
	 */
//	private RoadDTO road;
}
