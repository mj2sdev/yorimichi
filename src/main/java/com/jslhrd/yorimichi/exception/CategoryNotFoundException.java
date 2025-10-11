package com.jslhrd.yorimichi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotFoundException extends DomainException {

	public CategoryNotFoundException(Long categoryId) {
		super(
				"CATEGORY_NOT_FOUND",
				"카테고리를 찾을 수 없습니다.",
				Map.of("categoryId", categoryId)
		);
	}
}
