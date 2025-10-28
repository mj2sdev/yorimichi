package com.jslhrd.yorimichi.gemini.request;

import java.util.List;

public record StoreIngestRequest(
		String name,
		String description,
		String phone,
		Address address,
		List<String> categories,
		List<String> facilities,
		List<String> images,
		List<Food> foods,
		String idempotencyKey
) {
	public record Address(
			String roadAddressText,
			String jibunAddressText,
			String detail,
			Double latitude,
			Double longitude
	) {
	}

	public record Food(
			String name,
			Integer price,
			String description
	) {
	}
}