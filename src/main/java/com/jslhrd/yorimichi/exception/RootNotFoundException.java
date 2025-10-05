package com.jslhrd.yorimichi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RootNotFoundException extends RuntimeException {

	public RootNotFoundException(Long rootId) {
		super("Root not found: " + rootId);
	}
}
