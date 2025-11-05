package com.jslhrd.yorimichi.gemini.review.request;

public record ReviewKeywordExtractRequest(
		Integer days,
		Integer limitStores,
		Integer perStoreReviews,
		Integer topN,
		String mode // "all" | "hot"
) {
}