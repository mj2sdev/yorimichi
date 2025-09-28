package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 알림 DTO.
 *
 * <br>알림 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
public class NotificationDTO {

	/**
	 * PK: 알림 ID
	 */
	private Long id;

	/**
	 * FK: 행위자 ID
	 */
	private Long actorUserId;

	/**
	 * FK: 대상 루트 ID
	 */
	private Long rootId;

	/**
	 * FK: 수신자 ID
	 */
	private Long targetUserId;

	/**
	 * 알림 내용
	 */
	private String message;

	/**
	 * 생성일시 (DB 자동 생성)
	 */
	private LocalDateTime createdAt;

	/**
	 * 수정일시 (DB 자동 갱신)
	 */
	private LocalDateTime updatedAt;

	/**
	 * 읽음일시
	 */
	private LocalDateTime readAt;


	/**
	 * 유저
	 */
	private UserDTO user;

	/**
	 * 루트
	 */
	private RootDTO root;
}