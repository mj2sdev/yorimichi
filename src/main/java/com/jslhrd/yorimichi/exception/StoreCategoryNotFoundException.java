package com.jslhrd.yorimichi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class StoreCategoryNotFoundException extends RuntimeException {

	public StoreCategoryNotFoundException(Long storeId, Long categoryId) {
		super("StoreCategory not found store id " + storeId + " and category id " + categoryId);
	}
}