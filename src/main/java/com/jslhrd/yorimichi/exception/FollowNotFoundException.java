package com.jslhrd.yorimichi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FollowNotFoundException extends RuntimeException {

	public FollowNotFoundException(Long followerId, Long followeeId) {
		super("Follow not found follower id " + followerId + " and followee id " + followeeId);
	}
}
