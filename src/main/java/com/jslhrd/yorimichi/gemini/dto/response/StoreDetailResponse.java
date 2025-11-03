package com.jslhrd.yorimichi.gemini.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record StoreDetailResponse(
		// 기본 정보
		String placeId,
		String name,
		String phone,
		String description,

		// 주소(평탄화)
		String sidoName,
		String sigunguName,
		String emdName,
		String detail,
		String roadAddressText,
		String jibunAddressText,

		// 부가 정보
		List<String> categories,
		List<String> facilities,
		List<Menu> menus,
		List<String> images
) {
	private static String nonNullString(String value) {
		return value == null ? "" : value;
	}

	private static <T> List<T> nonNullList(List<T> value) {
		return value == null ? Collections.emptyList() : List.copyOf(value);
	}

	public StoreDetailResponse normalized() {

		List<String> ni = images == null ? List.of() :
				images.stream()
						.filter(u -> u != null)
						.map(String::trim)
						.filter(u -> !u.isBlank())
						.filter(u -> u.startsWith("http://") || u.startsWith("https://")) // ★ 절대 URL만
						.toList();

		return new StoreDetailResponse(
				nonNullString(placeId),
				nonNullString(name),
				nonNullString(phone),
				nonNullString(description),

				nonNullString(sidoName),
				nonNullString(sigunguName),
				nonNullString(emdName),
				nonNullString(detail),
				nonNullString(roadAddressText),
				nonNullString(jibunAddressText),

				nonNullList(categories),
				nonNullList(facilities),
				nonNullList(menus),
				nonNullList(ni)
		);
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public record Menu(
			String name,
			Integer price,
			String description
	) {
	}
}