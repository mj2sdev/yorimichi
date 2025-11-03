package com.jslhrd.yorimichi.gemini.store.dto.request;

public record StoreDetailRequest(
		String regionLabel,
		String storeName,
		String placeId
) {
}