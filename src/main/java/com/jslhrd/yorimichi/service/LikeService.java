package com.jslhrd.yorimichi.service;

/**
 * 좋아요 관련 서비스 입니다.
 *
 * @author mj2sdev
 * @since 1.0
 */
public interface LikeService {

	/**
	 * 가게를 좋아요 처리합니다.
	 *
	 * <p>
	 * userId, rootId 를 필수로 요구합니다.
	 */
	public void save(Long userId, Long rootId);

	/**
	 * 좋아요 삭제
	 *
	 * <p>
	 * userId, rootId 를 필수로 요구합니다.
	 */
	public void delete(Long userId, Long rootId);
}
