package com.jslhrd.yorimichi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CoeatNotFoundException extends RuntimeException {

	public CoeatNotFoundException(Long coeatId) {
		super("Coeat not found: " + coeatId);
	}
}