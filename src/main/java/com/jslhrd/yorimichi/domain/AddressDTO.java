package com.jslhrd.yorimichi.domain;

import com.jslhrd.yorimichi.validation.Create;
import com.jslhrd.yorimichi.validation.Update;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
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
public class AddressDTO {

	/**
	 * PK: 주소 ID
	 */
	@Null(groups = Create.class)
	@NotNull(groups = Update.class)
	private Long id;

	/**
	 * FK: 도로 ID
	 */
	@NotNull
	@Positive
	private Long roadId;

	/**
	 * FK: 우편번호 ID
	 */
	@NotNull
	@Positive
	private Long postalId;

	/**
	 * 상세주소(동/호수 등)
	 */
	@NotBlank
	private String detail;

	/**
	 * 도로명 전체 주소
	 */
	@NotBlank
	private String roadAddressText;

	/**
	 * 지번 전체 주소
	 */
	@NotBlank
	private String jibunAddressText;

	/**
	 * 위도 (-90.0 ~ +90.0)
	 */
	@NotNull
	@DecimalMin("-90.0")
	@DecimalMax("90.0")
	@Digits(integer = 3, fraction = 6)
	private BigDecimal latitude;

	/**
	 * 경도 (-180.0 ~ +180.0)
	 */
	@NotNull
	@DecimalMin("-180.0")
	@DecimalMax("180.0")
	@Digits(integer = 3, fraction = 6)
	private BigDecimal longitude;

	/**
	 * 생성일시 (DB 자동 생성)
	 */
	@PastOrPresent
	@Null(groups = Create.class)
	private LocalDateTime createdAt;

	/**
	 * 수정일시 (DB 자동 갱신)
	 */
	@PastOrPresent
	@Null(groups = Create.class)
	private LocalDateTime updatedAt;


	/**
	 * 주소의 상점
	 */
	private StoreDTO store;

	/**
	 * 주소의 우편번호
	 */
	private PostalDTO postal;

	/**
	 * 주소의 도로명
	 */
	private RoadDTO road;
}