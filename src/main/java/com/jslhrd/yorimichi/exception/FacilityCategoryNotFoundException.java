package com.jslhrd.yorimichi.exception;

import java.util.Map;

public class FacilityCategoryNotFoundException extends DomainException {

	public FacilityCategoryNotFoundException(Long facilityId) {
		super(
				"FACILITY_CATEGORY_NOT_FOUND",
				"시설 카테고리를 찾을 수 없습니다.",
				Map.of("facilityId", facilityId)
		);
	}
}
