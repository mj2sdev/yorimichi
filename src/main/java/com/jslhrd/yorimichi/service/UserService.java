package com.jslhrd.yorimichi.service;

import com.jslhrd.yorimichi.domain.SearchDTO;
import com.jslhrd.yorimichi.domain.UserDTO;

import java.util.List;

/**
 * 사용자(User) 정보 관련 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 * Spring Security의 UserDetails를 확장하여 인증/인가에 사용됩니다.
 *
 * @author mj2sdev
 * @version 1.1 {@code UserDetailsService} 추가
 * <p>
 * 유저 디테일서비스 인터페이스 확장, 그에따른 findByEmail 삭제
 * -> {@code UserDetailsService} 인터페이스 내부에 이미 loadUserByUsername(String username) 이 존재
 */
public interface UserService {

	/**
	 * 유저 리스트를 검색합니다. (보통 관리자 유저 관리 페이지 등 사용)
	 *
	 * @param search 검색 파라미터 모음
	 * @return {@code List<UserDTO>} 유저 리스트
	 */
	public List<UserDTO> findAll(SearchDTO search);

	/**
	 * 유저 상세 정보를 조회합니다.
	 *
	 * @param userId
	 * @return 유저 특정 정보 반환.
	 */
	public UserDTO findById(Long userId);

	/**
	 * 단순 헬퍼: 닉네임 중복 체크(도메인 관점)
	 */
	boolean isNicknameAvailable(String nickname);


	/**
	 * 기존 사용자 정보를 수정합니다.
	 *
	 * @param userId
	 * @param user   수정할 사용자 정보가 담긴 DTO
	 */
	public void update(Long userId, UserDTO user);
}