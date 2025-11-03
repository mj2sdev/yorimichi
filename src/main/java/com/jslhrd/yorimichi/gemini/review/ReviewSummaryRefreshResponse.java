package com.jslhrd.yorimichi.gemini.review;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReviewSummaryRefreshResponse(
		int hotRequested,
		int backlogRequested,
		int hotCandidates,
		int backlogCandidates,
		int updated,
		int skipped,
		int errors,
		List<Long> updatedStoreIds
) {
}