package com.jslhrd.yorimichi.gemini;

import com.jslhrd.yorimichi.gemini.dto.request.SaveStoresRequest;
import com.jslhrd.yorimichi.gemini.dto.response.SaveStoresResponse;
import com.jslhrd.yorimichi.gemini.dto.response.StoreDetailResponse;

public interface GeminiStoreService {

	public boolean save(Long emdId, StoreDetailResponse detail);

	public SaveStoresResponse saveAll(SaveStoresRequest request);
}
