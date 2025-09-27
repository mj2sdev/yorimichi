package com.jslhrd.yorimichi.domain;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 유저 DTO.
 *
 * <br>유저 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
public class UserDTO extends RootDTO {

	/**
	 * FK: 권한/역할 ID
	 */
	@NotNull
	@Positive
	private Long roleId;

	/**
	 * 유저 이메일/아이디
	 */
	@NotBlank
	@Email
	@Size(max = 100)
	private String email;

	/**
	 * 유저 비밀번호
	 */
	@Size(max = 256)
	private String password;

	/**
	 * 유저 닉네임
	 */
	@NotBlank
	@Size(max = 20)
	private String nickname;

	/**
	 * 유저 자기소개
	 */
	@Size(max = 2000)
	private String description;

	/**
	 * 마지막 로그인 일시
	 */
	@PastOrPresent
	private LocalDateTime lastLoginAt;


	/**
	 * 권한/역할
	 */
	private RoleDTO role;

	/**
	 * 소셜 아이디
	 */
	private SocialAccountDTO socialAccount;

	/**
	 * 리뷰 목록
	 */
	private List<ReviewDTO> reviews;

	/**
	 * 팔로잉 목록
	 */
	private List<UserDTO> following;

	/**
	 * 팔로우 목록
	 */
	private List<UserDTO> followers;

	/**
	 * 즐겨찾기 목록
	 */
	private List<StoreDTO> bookmark;

	/**
	 * 좋아요 목록
	 */
	private List<StoreDTO> likes;

	/**
	 * 같이먹기 목록
	 */
	private List<CoeatDTO> coeats;
}