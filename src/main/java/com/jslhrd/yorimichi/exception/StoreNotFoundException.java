package com.jslhrd.yorimichi.exception;

import java.util.Map;

public class StoreNotFoundException extends DomainException {

	public StoreNotFoundException(Long storeId) {
		super(
				"STORE_NOT_FOUND",
				"가게를 찾을 수 없습니다.",
				Map.of("storeId", storeId)
		);
	}
}
