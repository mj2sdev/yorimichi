package com.jslhrd.yorimichi.exception;

import java.util.Map;

public class FoodNotFoundException extends DomainException {

	public FoodNotFoundException(Long foodId) {
		super(
				"FOOD_NOT_FOUND",
				"음식를 찾을 수 없습니다.",
				Map.of("foodId", foodId)
		);
	}
}