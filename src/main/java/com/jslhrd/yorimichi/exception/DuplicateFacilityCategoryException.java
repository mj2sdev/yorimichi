package com.jslhrd.yorimichi.exception;

import java.util.Map;

public class DuplicateFacilityCategoryException extends DomainException {

	public DuplicateFacilityCategoryException(String facilityCategoryName) {
		super(
				"DUPLICATE_FACILITY_CATEGORY",
				"동일한 시설 카테고리가 이미 등록되어 있습니다.",
				Map.of("facilityCategoryName", facilityCategoryName)
		);
	}
}