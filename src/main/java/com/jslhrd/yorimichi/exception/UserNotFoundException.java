package com.jslhrd.yorimichi.exception;

import java.util.Map;

public class UserNotFoundException extends DomainException {

	public UserNotFoundException(Long userId) {
		super(
				"USER_NOT_FOUND",
				"사용자를 찾을 수 없습니다.",
				Map.of("userId", userId)
		);
	}
}