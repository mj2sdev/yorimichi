package com.jslhrd.yorimichi.gemini.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

	private static String trim(String s) {
		return s == null ? "" : s.trim();
	}

	private static String trimOrEmpty(String s) {
		String t = trim(s);
		return t.isEmpty() ? "" : t;
	}

	private static String normalizePlaceIdToDb(String id) {
		String t = trim(id);
		if (t.isEmpty()) return "";
		// 허용: "places/ChIJ..." 또는 "ChIJ..."
		if (t.startsWith("places/")) {
			String core = t.substring(7);
			return isValidPlaceIdCore(core) ? "places/" + core : "";
		} else {
			return isValidPlaceIdCore(t) ? "places/" + t : "";
		}
	}

	private static boolean isValidPlaceIdCore(String core) {
		// 너무 빡세면 ^ChI 로 완화 가능
		return core != null && core.matches("^ChIJ[0-9A-Za-z_-]{10,}$");
	}

	private static List<String> normalizeStringListToDb(List<String> in) {
		if (in == null) return List.of();
		return in.stream()
				.map(StoreDetailResponse::trim)
				.filter(s -> !s.isEmpty())
				.distinct()
				.limit(50)
				.toList();
	}

	private static int normalizePriceToDb(Integer price) {
		if (price == null || price < 0) return 0;
		return Math.min(price, 10_000_000);
	}

	private static List<Menu> normalizeMenusToDb(List<Menu> menus) {
		if (menus == null || menus.isEmpty()) return List.of();

		Map<String, Menu> dedupByName = new LinkedHashMap<>();
		for (Menu m : menus) {
			if (m == null) continue;
			String name = trim(m.name());
			if (name.isEmpty()) continue;
			int price = normalizePriceToDb(m.price());
			String desc = trim(m.description());
			dedupByName.putIfAbsent(name, new Menu(name, price, desc.isEmpty() ? "" : desc));
		}
		return List.copyOf(dedupByName.values());
	}

	private static List<String> normalizeUrlsToDb(List<String> images) {
		if (images == null) return List.of();
		return images.stream()
				.map(StoreDetailResponse::trim)
				.filter(s -> !s.isEmpty())
				.filter(u -> u.startsWith("http://") || u.startsWith("https://"))
				.distinct()
				.limit(50)
				.toList();
	}


	/**
	 * DB 저장용: 문자열은 "", 가격은 0, 리스트는 [] 로 정규화합니다.
	 */
	public StoreDetailResponse normalized() {
		return new StoreDetailResponse(
				normalizePlaceIdToDb(placeId), // 빈/무효 → ""
				trimOrEmpty(name),
				trimOrEmpty(phone),
				trimOrEmpty(description),

				trimOrEmpty(sidoName),
				trimOrEmpty(sigunguName),
				trimOrEmpty(emdName),
				trimOrEmpty(detail),
				trimOrEmpty(roadAddressText),
				trimOrEmpty(jibunAddressText),

				normalizeStringListToDb(categories),
				normalizeStringListToDb(facilities),
				normalizeMenusToDb(menus),
				normalizeUrlsToDb(images)
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