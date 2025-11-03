package com.jslhrd.yorimichi.gemini.store.dto.request;

import com.jslhrd.yorimichi.gemini.store.dto.response.StoreDetailResponse;

public record StoreRequest(
		Long emdId,
		StoreDetailResponse detail
) {
}