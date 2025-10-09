package com.jslhrd.yorimichi.exception;

import java.util.Map;

public class RootNotFoundException extends DomainException {

	public RootNotFoundException(Long rootId) {
		super(
				"ROOT_NOT_FOUND",
				"컨텐츠를 찾을 수 없습니다.",
				Map.of("rootId", rootId)
		);
	}
}
