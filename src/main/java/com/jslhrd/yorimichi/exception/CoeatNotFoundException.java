package com.jslhrd.yorimichi.exception;

import java.util.Map;

public class CoeatNotFoundException extends DomainException {

	public CoeatNotFoundException(Long coeatId) {
		super(
				"COEAT_NOT_FOUND",
				"같이먹기를 찾을 수 없습니다.",
				Map.of("coeatId", coeatId)
		);
	}
}