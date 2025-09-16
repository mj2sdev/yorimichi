package com.jslhrd.yorimichi.service;

import org.springframework.security.core.userdetails.UserDetails;

import com.jslhrd.yorimichi.domain.UserDTO;

/**
 * 사용자(User) 정보 관련 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 * Spring Security의 UserDetails를 확장하여 인증/인가에 사용됩니다.
 * @author MJ2
 * @since 2025.09.16
 */
public interface UserService extends UserDetails {
	
	/**
	 * 사용자 이름(username)으로 사용자 정보를 조회합니다.
	 * @param username 조회할 사용자 이름
	 * @return UserDTO 사용자 정보 객체
	 */
	public UserDTO findByUsername(String username);
	
	/**
	 * 새로운 사용자 정보를 저장(회원가입)합니다.
	 * @param dto 저장할 사용자 정보가 담긴 DTO
	 */
	public void saveUser(UserDTO dto);
	
	/**
	 * 기존 사용자 정보를 수정합니다.
	 * @param dto 수정할 사용자 정보가 담긴 DTO
	 */
	public void updateUser(UserDTO dto);
	
	/**
	 * 사용자 ID를 이용하여 사용자 정보를 삭제(탈퇴)합니다.
	 * @param userId 삭제할 사용자의 ID
	 */
	public void deleteUser(Long userId);
	
}
