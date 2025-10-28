package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.ReviewDTO;
import com.jslhrd.yorimichi.gemini.GeminiHelper;
import com.jslhrd.yorimichi.gemini.request.StoreIngestRequest;
import com.jslhrd.yorimichi.gemini.request.StoreNameRegionResponse;
import com.jslhrd.yorimichi.service.GeminiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeminiManager implements GeminiService {

	private final GeminiHelper geminiHelper;
	private final RegionManager regionManager;

	@Override
	public List<StoreNameRegionResponse> findStoreNamesByRegion(Long sidoId, Long sigunguId, Long emdId, int count) {

		String region = regionManager.findBySidoId(sidoId).getName();

		if (sigunguId != null) {
			region += regionManager.findBySigunguId(sigunguId).getName();
		}

		String emdName = null;
		if (emdId != null) {
			emdName = regionManager.findByEmdId(emdId).getName();
		}

		String emdHint = (emdName == null || emdName.isBlank()) ? "" :
				"(가능하면 읍면동 " + emdName + " 중심으로 추천하세요)\n";

		String prompt = """
				한국어로만 답하세요.
				대상 지역: %s
				반드시 위 지역(행정구역) 내의 맛집 %d개를 알려주세요.
				출력은 JSON 배열이며, 각 원소는 { "name": string, "region": string, "emd": string } 형식입니다.
				"region" 값은 정확히 "%s" 이어야 합니다.
				%s추가 텍스트 금지, 모호/중복/타지역 제외.
				""".formatted(region, count, region, emdHint);

		return geminiHelper.listNameRegionPairs(prompt, count);
	}

	@Override
	public StoreIngestRequest findStoreInfoByName(String region, String name) {
		return null;
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
	public List<String> findKeywordByReviews(List<ReviewDTO> reviews) {
		String prompt = "여러 가게의 리뷰인데 핵심 키워드 뽑아줘\n";
		String promptWithReviews = promptWithReviewContents(prompt, reviews, null);

		return geminiHelper.listStringPrompt(promptWithReviews);
	}

	private String promptWithReviewContents(
			String prompt,
			List<ReviewDTO> reviews,
			Integer count
	) {
		count = Optional.ofNullable(count).orElseGet(() -> geminiHelper.MAX_LENGTH);
		count = Math.min(count, geminiHelper.MAX_LENGTH);
		reviews = reviews.subList(0, Math.min(reviews.size(), count));

		return prompt + "\n\n" + String.join("\n", reviews.stream()
				.map(review -> review.getContent())
				.toList()
		);
	}

}