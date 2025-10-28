package com.jslhrd.yorimichi.gemini.request;

public record FindRegionStoreNamesRequest(
		Long sidoId,
		Long sigunguId,
		Long emdId,
		Integer count
) {
	public int normalizedCount() {
		return Math.min(Math.max(((count == null) ? 10 : count), 1), 20);
	}
}