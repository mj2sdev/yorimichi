package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 팔로우 DTO.
 *
 * <br>팔로우 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
public class FollowDTO {

	/**
	 * FK: 유저 ID
	 */
	private Long followerId;

	/**
	 * FK: 팔로우 대상 ID
	 */
	private Long followeeId;

	/**
	 * 팔로우 알림여부
	 */
	private Boolean notified;

	/**
	 * 생성일시 (DB 자동 생성)
	 */
	private LocalDateTime createdAt;

	/**
	 * 수정일시 (DB 자동 갱신)
	 */
	private LocalDateTime updatedAt;

	public FollowDTO(Long followerId, Long followeeId) {
		this.followerId = followerId;
		this.followeeId = followeeId;
	}
}