package com.jslhrd.yorimichi.domain;

import com.jslhrd.yorimichi.validation.Create;
import com.jslhrd.yorimichi.validation.Update;
import jakarta.validation.constraints.*;
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
	@Null(groups = Create.class)
	@NotNull(groups = Update.class)
	private Long id;

	/**
	 * FK: 행위자 ID
	 */
	@NotNull
	@Positive
	private Long actorUserId;

	/**
	 * FK: 대상 루트 ID
	 */
	@NotNull
	@Positive
	private Long rootId;

	/**
	 * FK: 수신자 ID
	 */
	@NotNull
	@Positive
	private Long targetUserId;

	/**
	 * 알림 내용
	 */
	@NotBlank
	private String message;

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
	 * 읽음일시
	 */
	@PastOrPresent
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