package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 이미지 DTO.
 *
 * <br>이미지 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ImageDTO {

	/**
	 * PK: 이미지 ID
	 */
	private Long id;

	/**
	 * 이미지 URL
	 */
	private String url;

	/**
	 * 생성일시 (DB 자동 생성)
	 */
	private LocalDateTime createdAt;

	/**
	 * 수정일시 (DB 자동 갱신)
	 */
	private LocalDateTime updatedAt;


	public ImageDTO(String url) {
		this.url = url;
	}
}