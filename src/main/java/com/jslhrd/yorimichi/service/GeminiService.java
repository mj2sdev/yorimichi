package com.jslhrd.yorimichi.service;

import com.jslhrd.yorimichi.domain.ReviewDTO;
import com.jslhrd.yorimichi.domain.StoreDTO;

import java.util.List;

/**
 * 해당 서비스는 Gemini API 기능을 정의하는 인터페이스 입니다.
 * 인터페이스를 구현하여 각종 기능을 구현해주시기 바랍니다.
 *
 * @author @mj2sdev
 * @since 1.0
 */
public interface GeminiService {

	/**
	 * 여러개의 리뷰를 모아 AI를 활용하여 요약합니다.
	 *
	 * @param reviews 요약될 리뷰 리스트를 요구합니다.
	 * @return {@code String} 형식의 AI 요약된 리뷰가 반환됩니다.
	 */
	public String summaryOfReviews(List<ReviewDTO> reviews);

	/**
	 * 지역 이름 (region) 으로 가게명 리스트를 지정한 갯수만큼 (count) 뽑아냅니다.
	 *
	 * @param region 지역명 입니다.
	 * @param count  몇개를 뽑아낼지를 결정합니다.
	 * @return {@code List<String>} 가게명 리스트를 반환합니다.
	 */
	public List<String> findStoreNamesByRegion(String region, String count);

	/**
	 * 가게명 (name) 으로 가게정보를 뽑아냅니다.
	 * StoreDTO 에 맞는 정보들을 뽑아내야 합니다.
	 *
	 * @param name 가게명 입니다.
	 * @return StoreDTO 형식의 데이터를 반환합니다.
	 */
	public StoreDTO findStoreInfoByName(String name);

	/**
	 * 리뷰리스트 (reviews) 를 제공하여 해당 리뷰들에서 키워드를 뽑아냅니다.
	 *
	 * @param reviews
	 * @return {@code List<String>} 추출된 키워드 리스트를 반환합니다.
	 */
	public List<String> findKeywordByReviews(List<ReviewDTO> reviews);
}
