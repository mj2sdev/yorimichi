package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

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
@Alias("UserDTO")
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
}