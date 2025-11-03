package com.jslhrd.yorimichi.gemini.store.dto.request;

public record RegionStoreRequest(
		Long sidoId,
		Long sigunguId,
		Long emdId,
		int count) {
}