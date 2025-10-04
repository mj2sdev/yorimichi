package com.jslhrd.yorimichi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateCoeatRequestException extends RuntimeException {

	public DuplicateCoeatRequestException(Long userId, Long coeatId) {
		super("CoeatRequest exists user id " + userId + " and coeat id " + coeatId);
	}
}
