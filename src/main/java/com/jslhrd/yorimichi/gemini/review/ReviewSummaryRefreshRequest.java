package com.jslhrd.yorimichi.gemini.review;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReviewSummaryRefreshRequest(
		Integer hotSize,    // 핫한 가게 건수 상한
		Integer backlogSize // 백로그(미요약) 건수 상한
) {
	public int hotOrDefault(int dft) {
		return hotSize == null ? dft : hotSize;
	}

	public int backlogOrDefault(int dft) {
		return backlogSize == null ? dft : backlogSize;
	}
}