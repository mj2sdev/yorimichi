package com.jslhrd.yorimichi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class LikeNotFoundException extends RuntimeException {

	public LikeNotFoundException(Long userId, Long rootId) {
		super("Like not found user id " + userId + " and root id " + rootId);
	}
}
