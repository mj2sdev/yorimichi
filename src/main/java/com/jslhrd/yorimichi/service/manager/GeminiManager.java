package com.jslhrd.yorimichi.service.manager;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jslhrd.yorimichi.config.GeminiHelper;
import com.jslhrd.yorimichi.domain.ReviewDTO;
import com.jslhrd.yorimichi.domain.StoreDTO;
import com.jslhrd.yorimichi.service.GeminiService;

import lombok.RequiredArgsConstructor;

// @Service
@RequiredArgsConstructor
public class GeminiManager implements GeminiService {

	private final GeminiHelper geminiHelper;

	private String promptWithReviewContents(
			String prompt,
			List<ReviewDTO> reviews,
			Integer count) {
		count = Optional.ofNullable(count).orElseGet(() -> geminiHelper.MAX_LENGTH);
		count = Math.min(count, geminiHelper.MAX_LENGTH);
		reviews = reviews.subList(0, Math.min(reviews.size(), count));

		return prompt + "\n\n" + String.join("\n", reviews.stream()
			.map(review -> review.getContent())
			.toList()
		);
	}

	/**
	 * 너무 많은 리뷰를 요약하면 토큰관련 문제가 생길 수 있으므로 10개로 제한합니다.
	 */
	@Override
	public String summaryOfReviews(List<ReviewDTO> reviews) {
		String prompt = "여러 가게의 리뷰인데 한 문단으로 요약해 줘\n";
		String promptWithReviews = promptWithReviewContents(prompt, reviews, 10);
		return geminiHelper.simplePrompt(promptWithReviews);
	}

	@Override
	public List<String> findStoreNamesByRegion(String region, Integer count) {
		String prompt = String.format(
			"(%s)지역의 맛집을 %d개 찾아줄래?",
			region,
			count
		);
		
		return geminiHelper.listStringPrompt(prompt);
	}

	@Override
	public StoreDTO findStoreInfoByName(String name) {
		String prompt = String.format(
			"(%s)가게의 정보를 찾아줄래?",
			name
		);

		return geminiHelper.storePrompt(prompt);
	}

	@Override
	public List<String> findKeywordByReviews(List<ReviewDTO> reviews) {
		String prompt = "여러 가게의 리뷰인데 핵심 키워드 뽑아줘\n";
		String promptWithReviews = promptWithReviewContents(prompt, reviews, null);
		
		return geminiHelper.listStringPrompt(promptWithReviews);
	}
	
}
