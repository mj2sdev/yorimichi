package com.jslhrd.yorimichi.service;

import com.jslhrd.yorimichi.domain.FeedCoeatDTO;

/**
 * 같이먹기 서비스 인터페이스입니다.
 * @author MJ2
 * @since 2025.09.16
 * 
 */
public interface CoeatService {
	
	/**
	 * 같이먹기 글 내용 저장
	 * @param FeedCoeatDTO
	 */
	public void saveCoeat(FeedCoeatDTO dto);

	/**
	 * feedDTO 를 통해 내용을 수정합니다.
	 * @param feedDTO
	 */
	public void updateCoeat(FeedCoeatDTO dto);
	
	/**
	 * feedId 를 통해 같이먹기 feed를 삭제합니다.
	 * @param FeedId
	 */
	public void deleteCoeat(Long feedId);

	/**
	 * 참여하고싶은 사람 (userId) 이 같이먹기 게시물(feedId)에 추가됩니다.
	 * @param userId
	 * @param FeedId
	 */
	public void joinCoeat(Long userId, Long feedId);
}
