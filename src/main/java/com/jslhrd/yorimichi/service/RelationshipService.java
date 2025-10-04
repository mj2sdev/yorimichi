package com.jslhrd.yorimichi.service;

import com.jslhrd.yorimichi.domain.UserDTO;

import java.util.List;

/**
 * Follow&Follower 관련 서비스 인터페이스입니다.
 * TODO: 임시 작성으로 검토 필요합니다.
 *
 * @author 3ll3702
 * @version 1.0 초안작성
 */
public interface RelationshipService {

	/**
	 * 사용자가 팔로우 한 유저 목록을 가져옵니다.
	 *
	 * @param userId
	 * @return
	 */
	public List<UserDTO> findFollowees(Long userId);

	/**
	 * 사용자를 팔로우 한 유저 목록을 가져옵니다.
	 *
	 * @param userId
	 * @return
	 */
	public List<UserDTO> findFollowers(Long userId);

	/**
	 * 사용자가 대상을 관계 목록에 저장합니다.
	 *
	 * @param followerId 사용자
	 * @param followeeId 대상
	 */
	public void saveFollow(Long followerId, Long followeeId);

	public void updateFollowNotification(Long followerId, Long followeeId, Boolean followed);

	/**
	 * 사용자가 대상과의 관계를 전부 삭제합니다.
	 *
	 * @param followerId 사용자
	 * @param followeeId 대상
	 */
	public void deleteFollow(Long followerId, Long followeeId);

	/**
	 * 사용자가 차단한 대상 목록을 가져옵니다.
	 *
	 * @param userId
	 */
	public List<UserDTO> findBlocks(Long userId);

	/**
	 * 사용자가 대상을 차단 목록에 저장합니다.
	 *
	 * @param blockerId 사용자
	 * @param blockeeId 대상
	 */
	public void saveBlock(Long blockerId, Long blockeeId);

	/**
	 * 사용자가 대상을 차단목록에서 삭제합니다.
	 *
	 * @param blockerId 사용자
	 * @param blockeeId 대상
	 */
	public void deleteBlock(Long blockerId, Long blockeeId);
}
