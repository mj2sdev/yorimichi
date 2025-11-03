package com.jslhrd.yorimichi.gemini.dto.response;

import java.util.List;

public record StoreDetailResponse(
		String placeId,
		String name,
		String phone,
		String description,
		String sidoName,
		String sigunguName,
		String emdName,
		String roadAddressText,
		String jubunAddressText,
		List<String> categories,
		List<String> facilities,
		List<Food> foods,
		List<String> images
) {
	public record Food(
			String name,
			Integer price,
			String description
	) {
	}
}