package com.jslhrd.yorimichi.exception;

import java.util.Map;

public class DuplicateCoeatRequestException extends DomainException {

	public DuplicateCoeatRequestException(Long userId, Long coeatId) {
		super(
				"DUPLICATE_COEAT_REQUEST",
				"이미 신청되어 있습니다.",
				Map.of(
						"userId", userId,
						"coeatId", coeatId
				)
		);
	}
}
