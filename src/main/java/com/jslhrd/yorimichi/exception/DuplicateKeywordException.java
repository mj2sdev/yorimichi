package com.jslhrd.yorimichi.exception;

import java.util.Map;

public class DuplicateKeywordException extends DomainException {

	public DuplicateKeywordException(String keywordName) {
		super(
				"DUPLICATE_KEYWORD",
				"동일한 키워드가 이미 등록되어 있습니다.",
				Map.of("keywordName", keywordName)
		);
	}
}
