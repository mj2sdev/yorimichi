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
	 * @param userId
	 * @return
	 */
	public List<UserDTO> findFollowById(Long userId);

	/**
	 * 사용자를 팔로우 한 유저 목록을 가져옵니다.
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserDTO> findFollowerById(Long userId);

	/**
	 * 사용자가 대상을 관계 목록에 저장합니다.
	 * 
	 * @param userId 사용자
	 * @param targetId 대상
	 */
	public void saveFollow(Long userid, Long targetId);
	
	/**
	 * 사용자가 대상과의 관계를 전부 삭제합니다.
	 * 
	 * @param userId 사용자
	 * @param targetId 대상
	 */
	public void delete(Long userid, Long targetId);

	/**
	 * 사용자가 차단한 대상 목록을 가져옵니다.
	 * @param userId
	 */
	public List<UserDTO> findBlockById(Long userId);

	/**
	 * 사용자가 대상을 차단 목록에 저장합니다.
	 * 
	 * @param userId 사용자
	 * @param targetId 대상
	 */
	public void saveBlock(Long userid, Long targetId);
	
	/**
	 * 사용자가 대상을 차단목록에서 삭제합니다.
	 * 
	 * @param userId 사용자
	 * @param targetId 대상
	 */
	public void deleteBlock(Long userid, Long targetId);
}
