package com.jslhrd.yorimichi.service;

import java.util.List;

import com.jslhrd.yorimichi.domain.ReviewDTO;
import com.jslhrd.yorimichi.domain.StoreDTO;

/**
 * @author MJ2
 * @since 2025-09-16
 * @note 해당 서비스는 Gemini API 기능을 정의하는 인터페이스 입니다.
 * 해당 인터페이스를 구현하여 각종 기능을 구현해주시기 바랍니다.
 */
public interface GeminiService {
	
	/**
	 * 리뷰
	 * @param dto 리뷰를 DTO형식으로 요청합니다.
	 * @return String 형식의 요약된 리뷰가 반환됩니다.
	 */
	public String summaryOfReviews(Long storeId);

	/**
	 * 지역 이름 (region) 으로 가게명 리스트를 갯수만큼 (count) 뽑아냅니다.
	 * @param region
	 * @param count
	 * @return 가게명(String) 리스트를 반환합니다.
	 */
	public List<String> findStoreNameByRegion(String region, String count);

	/**
	 * 가게명 (name) 으로 가게정보를 뽑아냅니다.
	 * @param name
	 * @return StoreDTO 형식의 데이터를 반환합니다.
	 */
	public StoreDTO findStoreInfoByName(String name);

	/**
	 * 리뷰리스트 (reviews) 를 제공하여 해당 리뷰들에서 키워드를 정해진 갯수(count)개 뽑아냅니다.
	 * @param reviews
	 * @return List<String> 형식의 키워드 리스트를 반환합니다.
	 */
	public List<String> findKeywordByReviews(List<ReviewDTO> reviews, Integer count);
}
