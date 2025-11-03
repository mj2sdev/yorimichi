package com.jslhrd.yorimichi.exception;

import com.jslhrd.yorimichi.enums.RegionKeyType;
import com.jslhrd.yorimichi.enums.RegionLevel;

import java.util.Map;

public class RegionNotFoundException extends DomainException {

	private RegionNotFoundException(String message, Map<String, Object> meta) {
		super("REGION_NOT_FOUND", message, meta);
	}

	public static RegionNotFoundException byId(RegionLevel level, Long id) {
		return new RegionNotFoundException(
				"지역을 찾을 수 없습니다.",
				Map.of(
						"level", level.name(),
						"keyType", RegionKeyType.ID.name(),
						"key", id
				)
		);
	}

	public static RegionNotFoundException byName(RegionLevel level, String name) {
		return new RegionNotFoundException(
				"지역을 찾을 수 없습니다.",
				Map.of(
						"level", level.name(),
						"keyType", RegionKeyType.NAME.name(),
						"key", name
				)
		);
	}

	public static RegionNotFoundException byCode(RegionLevel level, String code) {
		return new RegionNotFoundException(
				"지역을 찾을 수 없습니다.",
				Map.of(
						"level", level.name(),
						"keyType", RegionKeyType.CODE.name(),
						"key", code
				)
		);
	}
}