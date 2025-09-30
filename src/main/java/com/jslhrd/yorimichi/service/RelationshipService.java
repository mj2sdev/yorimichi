package com.jslhrd.yorimichi.service;

import java.security.Principal;
import java.util.List;

import com.jslhrd.yorimichi.domain.UserDTO;

/**
 * Follow&Follower 관련 서비스 인터페이스입니다.
 * TODO: 임시 작성으로 검토 필요합니다.
 * @author 3ll3702
 * 
 * @version 1.0 초안작성
 */
public interface RelationshipService {
	
	/**
	 * 사용자가 팔로우 한 유저 목록을 가져옵니다.
	 * 
	 * @param principal
	 * @return
	 */
	public List<UserDTO> findFollowById(Principal principal);

	/**
	 * 사용자를 팔로우 한 유저 목록을 가져옵니다.
	 * 
	 * @param principal
	 * @return
	 */
	public List<UserDTO> findFollowerById(Principal principal);

	/**
	 * 사용자가 대상을 관계 목록에 저장합니다.
	 * 
	 * @param principal
	 * @param userId
	 */
	public void saveFollow(Principal principal, Long userId);
	
	/**
	 * 사용자가 대상과의 관계를 전부 삭제합니다.
	 * 
	 * @param principal
	 * @param userId
	 */
	public void delete(Principal principal, Long userId);

	/**
	 * 사용자가 대상을 차단 목록에 저장합니다.
	 * 
	 * @param principal
	 * @param userId
	 */
	public void saveBlock(Principal principal, Long userId);
	
	/**
	 * 사용자가 대상을 차단목록에서 삭제합니다.
	 * 
	 * @param principal
	 * @param userId
	 */
	public void deleteBlock(Principal principal, Long userId);
}
