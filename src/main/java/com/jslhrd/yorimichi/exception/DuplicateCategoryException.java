package com.jslhrd.yorimichi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateCategoryException extends DomainException {

	public DuplicateCategoryException(String name) {
		super(
				"DUPLICATE_CATEGORY",
				"동일한 카테고리가 이미 등록되어 있습니다.",
				Map.of("name", name)
		);
	}
}
