package com.jslhrd.yorimichi.gemini.store;

import com.jslhrd.yorimichi.gemini.store.dto.request.SaveStoresRequest;
import com.jslhrd.yorimichi.gemini.store.dto.response.SaveStoresResponse;
import com.jslhrd.yorimichi.gemini.store.dto.response.StoreDetailResponse;

public interface GeminiStoreService {

	public boolean save(Long emdId, StoreDetailResponse detail);

	public SaveStoresResponse saveAll(SaveStoresRequest request);
}
