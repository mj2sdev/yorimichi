package com.jslhrd.yorimichi.domain;

import com.jslhrd.yorimichi.enums.Provider;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 소셜 계정 DTO.
 *
 * <br>소셜 계정 정보를 전달합니다.
 *
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
public class SocialAccountDTO extends RootDTO {

	/**
	 * PK1, FK: 권한/역할 ID
	 */
	private Long userId;

	/**
	 * PK2: 제공자
	 */
	private Provider provider;

	/**
	 * 제공자 유저 ID
	 */
	private String providerUserId;

	/**
	 * 제공자 유저 이메일
	 */
	private String providerEmail;

	/**
	 * 이메일 인증여부
	 */
	private String emailVerified;

	/**
	 * 제공자 유저 닉네임
	 */
	private String displayName;

	/**
	 * 제공자 유저 프로필 URL
	 */
	private String avatarUrl;

	/**
	 * 생성일시 (DB 자동 생성)
	 */
	private LocalDateTime createdAt;

	/**
	 * 수정일시 (DB 자동 갱신)
	 */
	private LocalDateTime updatedAt;

	/**
	 * 마지막 로그인 일시
	 */
	private LocalDateTime lastLoginAt;
}