package com.jslhrd.yorimichi.exception;

import java.util.Map;

public class DuplicateStoreException extends DomainException {
	public DuplicateStoreException(Long addressId, String storeName) {
		super(
				"DUPLICATE_STORE",
				"해당 주소에는 동일한 상호가 이미 등록되어 있습니다.",
				Map.of(
						"addressId", addressId,
						"storeName", storeName
				)
		);
	}
}
