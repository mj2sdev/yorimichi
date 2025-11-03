package com.jslhrd.yorimichi.gemini.dto.request;

public record StoreDetailRequest(
		String regionLabel,
		String storeName,
		String placeId
) {
}