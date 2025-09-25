package com.jslhrd.yorimichi.domain;

import com.jslhrd.yorimichi.enums.CoeatRequestStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 같이먹기 요청 DTO.
 * <p>
 * 같이먹기 요청 정보를 전닿합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
public class CoeatRequestDTO {

	/**
	 * PK, FK: 같이먹기 ID
	 */
	private Long coeatId;

	/**
	 * FK: 요청자 ID
	 */
	private Long userId;

	/**
	 * 같이먹기 요청 처리 상태
	 */
	private CoeatRequestStatus status;

	/**
	 * 같이먹기 요청 자기소개
	 */
	private String message;

	/**
	 * 생성시각 (DB 자동 생성)
	 */
	private LocalDateTime createdAt;

	/**
	 * 수정일시 (DB 자동 갱신)
	 */
	private LocalDateTime updatedAt;


	/**
	 * 신청한 같이먹기
	 */
	private CoeatDTO coeatDTO;

	/**
	 * 신청한 유저
	 */
	private UserDTO userDTO;
}