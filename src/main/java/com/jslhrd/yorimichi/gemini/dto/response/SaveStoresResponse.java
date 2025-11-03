package com.jslhrd.yorimichi.gemini.dto.response;

import java.util.List;

public record SaveStoresResponse(
		int savedCount,
		List<UnresolvedItem> unresolved
) {
	public record UnresolvedItem(
			int index,
			String storeName,
			String reason
	) {
	}
}