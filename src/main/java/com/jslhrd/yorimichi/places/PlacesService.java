package com.jslhrd.yorimichi.places;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jslhrd.yorimichi.gemini.GeminiService;
import com.jslhrd.yorimichi.gemini.store.dto.response.StoreDetailResponse;
import com.jslhrd.yorimichi.gemini.store.dto.response.StoreNameRegionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlacesService {

	private final RestClient placesRestClient;
	private final GeminiService geminiService;
	private final ObjectMapper objectMapper;


	// 내부 유틸
	private static String empty(String v) {
		return v == null ? "" : v;
	}

	// 목록: Text Search (필요 필드만)
	public List<StoreNameRegionResponse> searchStores(
			String query,
			String sidoName,
			String sigunguName,
			String emdName, int count
	) {
		String textQuery = query + " " + sidoName + " " + sigunguName + " " + emdName;

		Map<String, Object> requestBody = Map.of(
				"textQuery", textQuery,
				"pageSize", count,
				"regionCode", "KR",
				"languageCode", "ko"
		);

		String fieldMask = "places.id,places.displayName,places.addressComponents,places.types";

		Map<String, Object> response = placesRestClient.post()
				.uri("/v1/places:searchText")
				.header("X-Goog-FieldMask", fieldMask)
				.body(requestBody)
				.retrieve()
				.body(new ParameterizedTypeReference<>() {
				});

		List<Map<String, Object>> places = (List<Map<String, Object>>) response.get("places");
		List<StoreNameRegionResponse> results = new ArrayList<>();

		for (Map<String, Object> place : places) {
			String placeId = (String) place.get("id");
			Map<String, Object> displayName = (Map<String, Object>) place.get("displayName");
			String storeName = (String) displayName.get("text");

			List<Map<String, Object>> components = (List<Map<String, Object>>) place.get("addressComponents");
			String outSido = null, outSigungu = null, outEmd = null;
			for (Map<String, Object> c : components) {
				List<String> types = (List<String>) c.get("types");
				String longText = (String) c.get("longText");
				if (types.contains("administrative_area_level_1")) outSido = longText;
				if (types.contains("administrative_area_level_2")) outSigungu = longText;
				if (types.contains("sublocality") || types.contains("administrative_area_level_3")) outEmd = longText;
			}

			results.add(new StoreNameRegionResponse(storeName, outSido, outSigungu, outEmd, placeId));
			if (results.size() >= count) break;
		}
		return results;
	}

	// 상세: Places Details + Photos(media)
	private StoreDetailResponse getDetail(String placeId) {
		String fieldMask = "id,displayName,internationalPhoneNumber,formattedAddress,addressComponents,types,photos";

		Map<String, Object> place = placesRestClient.get()
				.uri("/v1/{name}", placeId) // name = "places/xxxx"
				.header("X-Goog-FieldMask", fieldMask)
				.retrieve()
				.body(new ParameterizedTypeReference<>() {
				});

		String id = (String) place.get("id");
		Map<String, Object> displayName = (Map<String, Object>) place.get("displayName");
		String name = (String) displayName.get("text");
		String phone = (String) place.get("internationalPhoneNumber");
		String formattedAddress = (String) place.get("formattedAddress");

		List<Map<String, Object>> components = (List<Map<String, Object>>) place.get("addressComponents");
		String outSido = null, outSigungu = null, outEmd = null;
		for (Map<String, Object> c : components) {
			List<String> types = (List<String>) c.get("types");
			String longText = (String) c.get("longText");
			if (types.contains("administrative_area_level_1")) outSido = longText;
			if (types.contains("administrative_area_level_2")) outSigungu = longText;
			if (types.contains("sublocality") || types.contains("administrative_area_level_3")) outEmd = longText;
		}

		List<String> categories = (List<String>) place.get("types");

		List<String> images = new ArrayList<>();
		List<Map<String, Object>> photos = (List<Map<String, Object>>) place.get("photos");
		int limit = Math.min(3, photos.size());
		for (int i = 0; i < limit; i++) {
			Map<String, Object> photo = photos.get(i);
			String photoName = (String) photo.get("name"); // "places/.../photos/..."
			Map<String, Object> media = placesRestClient.get()
					.uri("/v1/{name}/media?maxHeightPx=800&maxWidthPx=800", photoName)
					.retrieve()
					.body(new ParameterizedTypeReference<>() {
					});
			images.add((String) media.get("photoUri"));
		}

		return null;
	}

	// 상세 + Gemini 보강(설명/지번/메뉴) — 통합 버전
	public StoreDetailResponse getDetailEnriched(String placeId) {

		StoreDetailResponse detail = getDetail(placeId);

		String prompt = """
				한국어로만 작성.
				아래 가게 정보를 바탕으로 JSON만 반환:
				{
				  "description": "2~3문장 소개",
				  "menus": [{"name":"...", "price":12345, "description":"..."}],
				  "jibunAddressText": "지번 주소 또는 빈 문자열"
				}
				
				가게 정보:
				- 상호: %s
				- 전화: %s
				- 도로명 주소: %s
				- 시/도: %s, 시/군/구: %s, 읍/면/동: %s
				- 타입들: %s
				""".formatted(
				empty(detail.name()),
				empty(detail.phone()),
				empty(detail.roadAddressText()),
				empty(detail.sidoName()),
				empty(detail.sigunguName()),
				empty(detail.emdName()),
				String.join(", ", detail.categories() == null ? List.of() : detail.categories())
		);

		String raw = geminiService.generateWithBoth(prompt);
		String text = read(raw, "/candidates/0/content/parts/0/text");

		Map<String, Object> enriched = parse(text);
		String description = (String) enriched.get("description");
		String jubun = (String) enriched.get("jibunAddressText");
		List<StoreDetailResponse.Menu> foods =
				objectMapper.convertValue(enriched.get("menus"), new TypeReference<>() {
				});

		return null;
	}


	// 다건 상세 (간단 순차)
	public List<StoreDetailResponse> getDetails(List<String> placeIds) {
		List<StoreDetailResponse> list = new ArrayList<>();
		for (String pid : placeIds) list.add(getDetailEnriched(pid));
		return list;
	}

	private String read(String json, String pointer) {
		try {
			return objectMapper.readTree(json).at(pointer).asText();
		} catch (Exception e) {
			return "";
		}
	}

	private Map<String, Object> parse(String json) {
		try {
			return objectMapper.readValue(json, new TypeReference<>() {
			});
		} catch (Exception e) {
			return Map.of();
		}
	}
}