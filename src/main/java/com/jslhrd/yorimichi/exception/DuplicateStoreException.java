package com.jslhrd.yorimichi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateStoreException extends RuntimeException {
	public DuplicateStoreException(Long addressId, String name) {
		super("Store exists address id " + addressId + " and store name " + name);
	}
}
