package com.jslhrd.yorimichi.service;

/**
 * 같이먹기 서비스 인터페이스입니다.
 *
 * @author @mj2sdev
 * @since 1.0
 * TODO: FeedCoeatDTO가 바뀔 예정이므로 Object로 임시 변경
 */
public interface CoeatService {

	/**
	 * 같이먹기 글 내용 저장
	 *
	 * @param dto
	 */
	public void save(Object dto);

	/**
	 * feedDTO 를 통해 내용을 수정합니다.
	 *
	 * @param feedDTO
	 */
	public void updateCoeat(Object dto);

	/**
	 * feedId 를 통해 같이먹기 feed를 삭제합니다.
	 *
	 * @param FeedId
	 */
	public void deleteCoeat(Long feedId);

	/**
	 * 참여하고싶은 사람 (userId) 이 같이먹기 게시물(feedId)에 추가됩니다.
	 *
	 * @param userId
	 * @param FeedId
	 */
	public void joinCoeat(Long userId, Long feedId);
}
