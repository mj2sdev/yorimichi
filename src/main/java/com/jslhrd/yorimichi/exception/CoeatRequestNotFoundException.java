package com.jslhrd.yorimichi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CoeatRequestNotFoundException extends RuntimeException {

	public CoeatRequestNotFoundException(Long userId, Long coeatId) {
		super("CoeatRequest not found user id " + userId + " and coeat id " + coeatId);
	}
}
