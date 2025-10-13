package com.jslhrd.yorimichi.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jslhrd.yorimichi.enums.Provider;
import lombok.*;

import java.io.Serializable;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"userId", "provider"})
public class SocialAccountDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * PK1, FK: 유저 ID
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
	@JsonIgnore
	private String providerEmail;

	/**
	 * 이메일 인증여부
	 */
	private boolean emailVerified;

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

	private UserDTO user;

	public SocialAccountDTO(Provider provider, String providerUserId, String providerEmail, boolean emailVerified, String displayName, String avatarUrl) {
		this.provider = provider;
		this.providerUserId = providerUserId;
		this.providerEmail = providerEmail;
		this.emailVerified = emailVerified;
		this.displayName = displayName;
		this.avatarUrl = avatarUrl;
	}
}