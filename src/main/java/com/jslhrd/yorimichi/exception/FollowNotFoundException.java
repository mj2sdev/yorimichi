package com.jslhrd.yorimichi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class FollowNotFoundException extends DomainException {

	public FollowNotFoundException(Long followerId, Long followeeId) {
		super(
				"FOLLOW_NOT_FOUND",
				"팔로우를 찾을 수 없습니다.",
				Map.of(
						"followerId", followerId,
						"followeeId", followeeId
				)
		);
	}
}
