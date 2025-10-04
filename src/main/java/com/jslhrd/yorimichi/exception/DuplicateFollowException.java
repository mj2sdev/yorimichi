package com.jslhrd.yorimichi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateFollowException extends RuntimeException {

	public DuplicateFollowException(Long followerId, Long followeeId) {
		super("Follow exists follower id " + followerId + " and followee id " + followeeId);
	}
}
