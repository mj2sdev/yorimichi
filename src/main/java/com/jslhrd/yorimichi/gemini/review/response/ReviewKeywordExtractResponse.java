package com.jslhrd.yorimichi.gemini.review.response;

import java.util.List;

public record ReviewKeywordExtractResponse(
		int processedStores,      // 처리한 가게 수
		int extractedKeywords,    // 추출된 키워드 총합 (중복 포함)
		int createdKeywords,      // 새로 생성된 키워드 수
		int linkedPairs,          // 루트-키워드 링크 시도(성공) 수
		int skippedStores,        // 리뷰 없음/키워드 없음 등으로 스킵
		int errors,               // 에러 발생한 가게 수
		List<Long> updatedStoreIds // 처리 대상 가게 ids
) {
}