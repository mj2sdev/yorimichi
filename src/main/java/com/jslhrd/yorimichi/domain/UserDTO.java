package com.jslhrd.yorimichi.domain;

import com.jslhrd.yorimichi.enums.RootType;
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
	private Long roleId;

	/**
	 * 유저 이메일/아이디
	 */
	private String email;

	/**
	 * 유저 비밀번호
	 */
	private String password;

	/**
	 * 유저 닉네임
	 */
	private String nickname;

	/**
	 * 유저 자기소개
	 */
	private String description;

	/**
	 * 마지막 로그인 일시
	 */
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

	/**
	 * 리뷰 공개 여부
	 */
	private boolean reviewPrivacy;
    
	/**
	 * 좋아요 공개 여부
	 */
    private boolean likePrivacy;
    
	/**
	 * 즐겨찾기 공개 여부
	 */
    private boolean bookmarkPrivacy;
    
	/**
	 * 팔로우 공개 여부
	 */
    private boolean followPrivacy;
    
	/**
	 * 팔로워 공개 여부
	 */
    private boolean followerPrivacy;


	public UserDTO() {
		super(RootType.USER);
	}
}
