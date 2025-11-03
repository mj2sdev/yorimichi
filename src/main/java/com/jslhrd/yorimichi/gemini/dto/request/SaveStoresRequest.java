package com.jslhrd.yorimichi.gemini.dto.request;

import com.jslhrd.yorimichi.gemini.dto.response.StoreDetailResponse;

import java.util.List;

public record SaveStoresRequest(
		Long sidoId,
		Long sigunguId,
		Long emdId,
		List<StoreDetailResponse> details
) {

}
