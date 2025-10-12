package com.jslhrd.yorimichi.exception;

import java.util.Map;

public class KeywordNotFoundException extends DomainException {

	public KeywordNotFoundException(Long keywordId) {
		super(
				"KEYWORD_NOT_FOUND",
				"키워드를 찾을 수 없습니다.",
				Map.of("keywordId", keywordId)
		);
	}
}
