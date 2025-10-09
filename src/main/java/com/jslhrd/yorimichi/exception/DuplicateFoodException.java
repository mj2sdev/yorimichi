package com.jslhrd.yorimichi.exception;

import java.util.Map;

public class DuplicateFoodException extends DomainException {
	public DuplicateFoodException(Long storeId, String foodName) {
		super(
				"DUPLICATE_FOOD",
				"해당 가게에는 동일한 음식이 이미 등록되어 있습니다.",
				Map.of(
						"storeId", storeId,
						"foodName", foodName
				)
		);
	}
}
