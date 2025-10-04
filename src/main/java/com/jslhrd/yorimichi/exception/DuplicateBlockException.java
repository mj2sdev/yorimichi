package com.jslhrd.yorimichi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateBlockException extends RuntimeException {

	public DuplicateBlockException(Long blockerId, Long blockeeId) {
		super("Follow exists blocker id " + blockerId + " and blockee id " + blockeeId);
	}
}
