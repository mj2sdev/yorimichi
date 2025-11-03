package com.jslhrd.yorimichi.gemini;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jslhrd.yorimichi.domain.RegionEmdDTO;
import com.jslhrd.yorimichi.domain.RegionSidoDTO;
import com.jslhrd.yorimichi.domain.RegionSigunguDTO;
import com.jslhrd.yorimichi.gemini.dto.RegionNames;
import com.jslhrd.yorimichi.gemini.dto.request.RegionStoreRequest;
import com.jslhrd.yorimichi.gemini.dto.request.StoreDetailRequest;
import com.jslhrd.yorimichi.gemini.dto.response.StoreDetailResponse;
import com.jslhrd.yorimichi.gemini.dto.response.StoreNameRegionResponse;
import com.jslhrd.yorimichi.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminService {

	private final RegionService regionService;
	private final GeminiService geminiService;
	private final ObjectMapper objectMapper;

	// ----- utils -----
	private static String joinSkippingBlank(String separator, String... parts) {
		return Arrays.stream(parts)
				.filter(Objects::nonNull)
				.map(String::trim)
				.filter(s -> !s.isBlank())
				.reduce((a, b) -> a + separator + b)
				.orElse("");
	}

	private static String removeCodeFence(String source) {
		if (source == null) return "";
		String s = source.trim();
		if (s.startsWith("```")) {
			int firstNl = s.indexOf('\n');
			if (firstNl > 0) s = s.substring(firstNl + 1);
			int lastFence = s.lastIndexOf("```");
			if (lastFence >= 0) s = s.substring(0, lastFence);
		}
		return s.trim();
	}

	private static String nz(String s) {
		return s == null ? "" : s;
	}

	private static StoreDetailResponse normalizeCollections(StoreDetailResponse in) {
		List<String> categories = in.categories() != null ? in.categories() : List.of();
		List<String> facilities = in.facilities() != null ? in.facilities() : List.of();
		List<StoreDetailResponse.Food> foods = in.foods() != null ? in.foods() : List.of();
		List<String> images = in.images() != null ? in.images() : List.of();

		return new StoreDetailResponse(
				in.placeId(),
				nz(in.name()),
				nz(in.phone()),
				nz(in.description()),
				nz(in.sidoName()),
				nz(in.sigunguName()),
				nz(in.emdName()),
				nz(in.roadAddressText()),
				nz(in.jubunAddressText()),
				categories,
				facilities,
				foods,
				images
		);
	}

	// ----- region name resolver -----
	private RegionNames resolveRegionNames(RegionStoreRequest request) {
		RegionSidoDTO sido = regionService.findBySidoId(request.sidoId());
		if (sido == null) throw new IllegalArgumentException("유효하지 않은 sidoId");

		String sidoName = sido.getName();
		String sigunguName = null;
		String emdName = null;

		if (request.emdId() != null) {
			RegionEmdDTO emd = regionService.findByEmdId(request.emdId());
			if (emd == null) throw new IllegalArgumentException("유효하지 않은 emdId");
			emdName = emd.getName();

			if (request.sigunguId() == null) {
				RegionSigunguDTO parentSigungu = regionService.findBySigunguId(emd.getSigunguId());
				sigunguName = parentSigungu != null ? parentSigungu.getName() : null;
			}
		}

		if (request.sigunguId() != null) {
			RegionSigunguDTO sigungu = regionService.findBySigunguId(request.sigunguId());
			if (sigungu == null || !Objects.equals(sigungu.getSidoId(), request.sidoId())) {
				throw new IllegalArgumentException("sidoId/sigunguId 불일치");
			}
			sigunguName = sigungu.getName();
		}

		return new RegionNames(sidoName, sigunguName, emdName);
	}

	// ----- list: find stores with Gemini (Search + Maps tools) -----
	public List<StoreNameRegionResponse> findStores(RegionStoreRequest request) {
		RegionNames regionNames = resolveRegionNames(request);
		String regionLabel = joinSkippingBlank(" ",
				regionNames.sidoName(),
				regionNames.sigunguName(),
				regionNames.emdName());

		int count = Math.max(1, Math.min(20, request.count()));
		String prompt = """
				한국어로만 작성.
				
				아래 지역에서 현지 평이 좋은 맛집을 정확히 %d곳 선정해 주세요.
				
				출력 규칙:
				- 출력은 JSON 배열 리터럴 하나만. 앞뒤 공백/문장/주석/코드펜스(``` 등) 금지.
				- 배열 길이는 정확히 %d.
				- 각 요소 객체는 아래 5개 키만 포함(철자·대소문자·순서 엄수), null/빈 문자열 금지:
				  {
				    "storeName": "string",
				    "sidoName": "string",
				    "sigunguName": "string",
				    "emdName": "string",
				    "placeId": "string"
				  }
				- placeId는 반드시 Google Place ID만 사용 (형식: "places/ChIJ...").
				- 행정명칭은 대한민국 공식 표기(시/도, 시/군/구, 읍/면/동).
				- 요청 지역 내부 장소만 선정, 중복 placeId 금지.
				- 조건을 만족할 수 없으면 빈 배열([]) 반환.
				
				요청 지역:
				- %s
				""".formatted(count, count, regionLabel);

		String raw = geminiService.generateWithBoth(prompt);

		try {
			JsonNode root = objectMapper.readTree(raw);
			String text = removeCodeFence(root.at("/candidates/0/content/parts/0/text").asText(""));

			// 본문이 비었으면 grounding에서 폴백 추출
			if (text.isBlank() || "[]".equals(text)) {
				return fallbackFromGrounding(root,
						regionNames.sidoName(),
						regionNames.sigunguName(),
						regionNames.emdName(),
						count);
			}

			List<StoreNameRegionResponse> parsed = objectMapper.readValue(
					text, new TypeReference<>() {
					}
			);
			return parsed.size() > count ? parsed.subList(0, count) : parsed;

		} catch (Exception e) {
			return List.of();
		}
	}

	private List<StoreNameRegionResponse> fallbackFromGrounding(
			JsonNode root, String sido, String sigungu, String emd, int need
	) {
		LinkedHashMap<String, String> ordered = new LinkedHashMap<>();

		for (JsonNode ch : root.at("/candidates/0/groundingMetadata/groundingChunks")) {
			JsonNode maps = ch.get("maps");
			if (maps == null) continue;

			String placeId = maps.path("placeId").asText("");
			String title = maps.path("title").asText("");

			// Google Place ID 형태만 채택
			if (!placeId.startsWith("places/")) continue;

			ordered.putIfAbsent(placeId, title);
			if (ordered.size() >= need * 2) break; // 약간 여유 수집
		}

		List<StoreNameRegionResponse> out = new ArrayList<>();
		for (Map.Entry<String, String> e : ordered.entrySet()) {
			if (out.size() >= need) break;
			out.add(new StoreNameRegionResponse(e.getValue(), sido, sigungu, emd, e.getKey()));
		}
		return out;
	}

	// ----- detail: one store with Gemini (Search + Maps tools) -----
	public Optional<StoreDetailResponse> storeDetailByRegionNameAndPlaceId(StoreDetailRequest request) {
		String regionLabel = request.regionLabel();
		String storeName = request.storeName();
		String placeId = request.placeId();

		String prompt = """
				한국어로만 작성.
				
				아래 조건을 모두 만족하는 가게 1곳의 상세 정보만 JSON 배열로 반환해 주세요.
				- 지역(행정명): %s
				- 가게명(검색 힌트): %s
				- 반드시 이 placeId와 정확히 일치: %s   (형식: "places/ChIJ...")
				
				출력 규칙(엄격):
				- 출력은 JSON 배열 리터럴 하나만. 앞뒤 공백/문장/주석/코드펜스(``` 등) 금지.
				- 배열 길이는 정확히 1. 불확실하면 빈 배열([]).
				- 키/순서/철자 엄수. null 최소화(모를 때만 빈 문자열/빈 배열).
				[
				  {
				    "placeId": "string",          // 반드시 %s 와 정확 일치
				    "name": "string",
				    "phone": "string",
				    "description": "string",
				    "sidoName": "string",
				    "sigunguName": "string",
				    "emdName": "string",
				    "roadAddressText": "string",
				    "jubunAddressText": "string",
				    "categories": ["string"],
				    "facilities": ["string"],
				    "foods": [
				      { "name": "string", "price": 12345, "description": "string" }
				    ],
				    "images": ["string"]
				  }
				]
				
				주의:
				- placeId가 %s 와 다르면 무조건 빈 배열([]).
				- 지역/상호 불일치도 빈 배열([]).
				- 불확실한 필드는 빈 문자열("") 또는 빈 배열([]).
				""".formatted(regionLabel, storeName, placeId, placeId, placeId);

		String raw = geminiService.generateWithBoth(prompt);

		try {
			JsonNode root = objectMapper.readTree(raw);
			String body = removeCodeFence(root.at("/candidates/0/content/parts/0/text").asText("")).trim();

			if (body.isBlank() || "[]".equals(body)) return Optional.empty();

			List<StoreDetailResponse> list = objectMapper.readValue(body, new TypeReference<>() {
			});
			if (list.isEmpty()) return Optional.empty();

			StoreDetailResponse first = list.get(0);
			if (first.placeId() == null || !first.placeId().equals(placeId)) return Optional.empty();

			return Optional.of(normalizeCollections(first));

		} catch (Exception e) {
			return Optional.empty();
		}
	}
}