package com.jslhrd.yorimichi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateStoreCategoryException extends RuntimeException {

	public DuplicateStoreCategoryException(Long storeId, Long categoryId) {
		super("StoreCategory exists store id " + storeId + " and category id " + categoryId);
	}
}
