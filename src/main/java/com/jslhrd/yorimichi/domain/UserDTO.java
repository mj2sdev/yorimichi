package com.jslhrd.yorimichi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jslhrd.yorimichi.enums.Role;
import com.jslhrd.yorimichi.enums.RootType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
@ToString(exclude = "password")
public class UserDTO extends RootDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 권한/역할
	 */
	private Role role;

	/**
	 * 유저 이메일/아이디
	 */
	private String email;

	/**
	 * 유저 비밀번호
	 */
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	/**
	 * 유저 닉네임
	 */
	private String nickname;

	/**
	 * 유저 자기소개
	 */
	private String description;


	private String gender;

	private LocalDate year;

	private boolean emailVerified;

	private boolean enabled;

	private boolean showReviews;

	private boolean showLikes;

	private boolean showBookmarks;

	private boolean showFollowing;

	private boolean showFollowers;

	/**
	 * 마지막 로그인 일시
	 */
	private LocalDateTime lastLoginAt;


	/**
	 * 소셜 아이디 목록
	 */
	private List<SocialAccountDTO> socialAccounts = new ArrayList<>();

	/**
	 * 리뷰 목록
	 */
	private List<ReviewDTO> reviews = new ArrayList<>();

	/**
	 * 팔로잉 목록
	 */
	private List<UserDTO> following = new ArrayList<>();

	/**
	 * 팔로우 목록
	 */
	private List<UserDTO> followers = new ArrayList<>();

	/**
	 * 즐겨찾기 목록
	 */
	private List<StoreDTO> bookmark = new ArrayList<>();

	/**
	 * 좋아요 목록
	 */
	private List<StoreDTO> likes = new ArrayList<>();

	/**
	 * 같이먹기 목록
	 */
	private List<CoeatDTO> coeats = new ArrayList<>();


	public UserDTO() {
		super(RootType.USER);
	}
}