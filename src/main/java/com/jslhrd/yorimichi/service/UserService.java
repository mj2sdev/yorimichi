package com.jslhrd.yorimichi.service;

import java.util.List;

import com.jslhrd.yorimichi.domain.SearchDTO;
import com.jslhrd.yorimichi.domain.StoreDTO;
import com.jslhrd.yorimichi.domain.UserDTO;

/**
 * 사용자(User) 정보 관련 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 * Spring Security의 UserDetails를 확장하여 인증/인가에 사용됩니다.
 * @author MJ2
 * @since 2025.09.16
 */
public interface UserService {

	/**
	 * 유저가 좋아요(Like) 한 가게 정보 리스트를 불러옵니다. 
	 * @param userId
	 * @return List<StoreDTO> 가게정보 리스트
	 */
	public List<StoreDTO> findLikedStoresByUserId(Long userId);
	
	/**
	 * 유저 리스트를 검색합니다. (보통 관리자 유저 관리 페이지 등 사용)
	 * @param dto 검색 파라미터 모음
	 * @return List<UserDTO> 유저 리스트
	 */
	public List<UserDTO> findUsers(SearchDTO dto);
	
	/**
	 * 사용자 이메일(email)로 사용자 정보를 조회합니다.
	 * @param email 조회할 사용자 이름
	 * @return UserDTO 사용자 정보 객체
	 */
	public UserDTO findUserByEmail(String eamil);
	
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
