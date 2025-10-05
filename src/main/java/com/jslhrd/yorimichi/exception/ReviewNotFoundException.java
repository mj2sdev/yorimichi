package com.jslhrd.yorimichi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReviewNotFoundException extends RuntimeException {

	public ReviewNotFoundException(Long reviewId) {
		super("Review not found: " + reviewId);
	}
}
