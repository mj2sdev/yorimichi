package com.jslhrd.yorimichi.domain;

import com.jslhrd.yorimichi.enums.ProviderName;
import com.jslhrd.yorimichi.validation.Create;
import jakarta.validation.constraints.*;
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
	 * PK1, FK: 유저 ID
	 */
	@NotNull
	@Positive
	private Long userId;

	/**
	 * PK2: 제공자
	 */
	@NotBlank
	@Size(max = 20)
	private ProviderName provider;

	/**
	 * 제공자 유저 ID
	 */
	@NotBlank
	@Size(max = 191)
	private String providerUserId;

	/**
	 * 제공자 유저 이메일
	 */
	@Email
	@Size(max = 191)
	private String providerEmail;

	/**
	 * 이메일 인증여부
	 */
	@NotNull
	private String emailVerified;

	/**
	 * 제공자 유저 닉네임
	 */
	@Size(max = 100)
	private String displayName;

	/**
	 * 제공자 유저 프로필 URL
	 */
	@Size(max = 256)
	private String avatarUrl;

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
	 * 마지막 로그인 일시
	 */
	@PastOrPresent
	private LocalDateTime lastLoginAt;
}