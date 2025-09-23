package com.jslhrd.yorimichi.service;

import com.jslhrd.yorimichi.domain.LikeDTO;

/**
 * 좋아요 관련 서비스 입니다.
 * 
 * @author mj2sdev
 * @since 1.0
 */
public interface LikeServie {
	
	/**
	 * 가게를 좋아요 처리합니다. 
	 * 
	 * @param dto 좋아요 정보
	 * <p>
	 * userId, rootId 를 필수로 요구합니다.
	 */
	public void save(LikeDTO dto);

	/**
	 * 좋아요 삭제
	 * 
	 * @param dto 좋아요 정보
	 * <p>
	 * userId, rootId 를 필수로 요구합니다.
	 */
	public void delete(LikeDTO dto);
}
