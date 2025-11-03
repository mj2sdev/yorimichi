package com.jslhrd.yorimichi.gemini.dto.request;

import com.jslhrd.yorimichi.gemini.dto.response.StoreDetailResponse;

public record StoreRequest(
		Long emdId,
		StoreDetailResponse detail
) {
}