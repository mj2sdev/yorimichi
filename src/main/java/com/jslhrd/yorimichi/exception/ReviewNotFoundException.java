package com.jslhrd.yorimichi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReviewNotFoundException extends DomainException {

	public ReviewNotFoundException(Long reviewId) {
		super(
				"REVIEW_NOT_FOUND",
				"",
				Map.of("reviewId", reviewId)
		);
	}
}
