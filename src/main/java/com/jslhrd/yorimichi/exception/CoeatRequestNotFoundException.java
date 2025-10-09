package com.jslhrd.yorimichi.exception;

import java.util.Map;

public class CoeatRequestNotFoundException extends DomainException {

	public CoeatRequestNotFoundException(Long userId, Long coeatId) {
		super(
				"COEAT_REQUEST_NOT_FOUND",
				"같이먹기 신청을 찾을 수 없습니다.",
				Map.of(
						"userId", userId,
						"coeatId", coeatId
				)
		);
	}
}