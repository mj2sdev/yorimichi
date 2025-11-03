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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

	private static final String KOREAN_RULES = """
			한국어만 사용하세요. 영어/기타 언어 금지.
			아래 어느 하나라도 지키지 못하면 빈 배열([])을 반환하세요.
			- 설명/카테고리/시설/주소는 한국어로 표기 (고유명은 원문 허용).
			- 영어 설명이 필요하면 한국어로 의역하고, 원문은 생략.
			- 출력은 JSON 배열 리터럴 하나만 (앞뒤 텍스트/코드펜스/주석 금지)
			- 한국어 표기만 사용 (상호 고유명은 원문 허용)
			""";

	private final RegionService regionService;
	private final GeminiService geminiService;
	private final ObjectMapper objectMapper;

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
		String trimmed = source.trim();
		if (trimmed.startsWith("```")) {
			int firstNewLineIndex = trimmed.indexOf('\n');
			if (firstNewLineIndex > 0) trimmed = trimmed.substring(firstNewLineIndex + 1);
			int lastFenceIndex = trimmed.lastIndexOf("```");
			if (lastFenceIndex >= 0) trimmed = trimmed.substring(0, lastFenceIndex);
		}
		return trimmed.trim();
	}

	private static String sanitizeRegionLabel(String regionLabel) {
		if (regionLabel == null) return "";
		// 셀렉트 placeholder가 끼어들어온 경우 제거
		String cleaned = regionLabel
				.replace("시군구를 선택하세요", "")
				.replace("읍면동을 선택하세요", "")
				.replaceAll("\\s+", " ")
				.trim();
		return cleaned;
	}

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

	public List<StoreNameRegionResponse> findStores(RegionStoreRequest request) {
		RegionNames regionNames = resolveRegionNames(request);
		String regionLabel = joinSkippingBlank(" ",
				regionNames.sidoName(),
				regionNames.sigunguName(),
				regionNames.emdName());

		int count = Math.max(1, Math.min(20, request.count()));
		String prompt = KOREAN_RULES + """
				
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

			if (text.isBlank() || "[]".equals(text)) {
				return fallbackFromGrounding(
						root,
						regionNames.sidoName(),
						regionNames.sigunguName(),
						regionNames.emdName(),
						count
				);
			}

			List<StoreNameRegionResponse> parsed = objectMapper.readValue(text, new TypeReference<>() {
			});
			return parsed.size() > count ? parsed.subList(0, count) : parsed;

		} catch (Exception e) {
			log.warn("findStores: parsing failed, return empty. cause={}", e.toString());
			return List.of();
		}
	}

	private List<StoreNameRegionResponse> fallbackFromGrounding(
			JsonNode root,
			String sido,
			String sigungu,
			String emd,
			int need
	) {
		LinkedHashMap<String, String> ordered = new LinkedHashMap<>();

		for (JsonNode chunk : root.at("/candidates/0/groundingMetadata/groundingChunks")) {
			JsonNode maps = chunk.get("maps");
			if (maps == null) continue;

			String placeId = maps.path("placeId").asText("");
			String title = maps.path("title").asText("");

			if (!placeId.startsWith("places/")) continue;

			ordered.putIfAbsent(placeId, title);
			if (ordered.size() >= need * 2) break;
		}

		List<StoreNameRegionResponse> out = new ArrayList<>();
		for (Map.Entry<String, String> entry : ordered.entrySet()) {
			if (out.size() >= need) break;
			out.add(new StoreNameRegionResponse(entry.getValue(), sido, sigungu, emd, entry.getKey()));
		}
		return out;
	}

	public Optional<StoreDetailResponse> storeDetail(StoreDetailRequest request) {

		String requiredPlaceId = request.placeId();
		String regionLabelClean = sanitizeRegionLabel(request.regionLabel());
		String storeNameHint = request.storeName();

		String strictPrompt = KOREAN_RULES + """
				
				아래 조건을 모두 만족하는 가게 1곳의 상세 정보만 JSON 배열로 반환해 주세요.
				- 지역(행정명): %s
				- 가게명(검색 힌트): %s
				- 반드시 이 placeId와 정확히 일치: %s   (형식: "places/ChIJ...")
				
				출력 규칙(엄격):
				- 출력은 JSON 배열 리터럴 하나만. 앞뒤 공백/문장/주석/코드펜스(``` 등) 금지.
				- 배열 길이는 정확히 1. 불확실하면 빈 배열([]).
				- 키/순서/철자 엄수. null 최소화(모를 때만 빈 문자열/빈 배열).
				- 가능하면 실제 판매 메뉴 이름을 3~8개 수집해 "menus"에 담으세요. 가격/설명이 불확실하면 생략하고 이름만 넣으세요.
				[
				  {
				    "name": "string",
				    "phone": "string",
				    "description": "string",
				    "categories": ["string"],
				    "facilities": ["string"],
				    "menus": [ { "name": "string", "price": 12345, "description": "string" } ],
				    "images": ["string"],
				    "sidoName": "string",
				    "sigunguName": "string",
				    "emdName": "string",
				    "detail": "string",
				    "placeId": "string",            // 반드시 %s 와 정확 일치
				    "roadAddressText": "string",
				    "jibunAddressText": "string"
				  }
				]
				
				주의:
				- placeId가 %s 와 다르면 무조건 빈 배열([]).
				- 지역/상호 불일치도 빈 배열([]).
				- menus 배열은 0개 이상 허용, 단 각 항목의 "name"은 반드시 존재/비공백.
				- price/description 은 모르면 생략하거나 null.
				- 불확실한 필드는 빈 문자열("") 또는 빈 배열([]).
				""".formatted(regionLabelClean, storeNameHint, requiredPlaceId, requiredPlaceId, requiredPlaceId);

		String relaxedPrompt = KOREAN_RULES + """
				
				아래 placeId에 해당하는 가게 1곳의 상세 정보를 JSON 배열로 반환해 주세요.
				- placeId(필수): %s
				- 지역(참고용): %s
				- 가게명(힌트): %s
				
				출력 규칙:
				- 출력은 JSON 배열 리터럴 하나, 길이 1(불확실하면 빈 배열 []).
				- 각 객체는 아래와 같은 단순(flat) 키만 사용:
				- 가능하면 실제 판매 메뉴 이름을 3~8개 수집해 "menus"에 담으세요. 가격/설명이 불확실하면 생략하고 이름만 넣으세요.
				[
				  {
				    "name": "string",
				    "phone": "string",
				    "description": "string",
				    "categories": ["string"],
				    "facilities": ["string"],
				    "menus": [ { "name": "string", "price": 12345, "description": "string" } ],
				    "images": ["string"],
				    "sidoName": "string",
				    "sigunguName": "string",
				    "emdName": "string",
				    "detail": "string",
				    "placeId": "string",        // 반드시 %s 와 동일
				    "roadAddressText": "string",
				    "jibunAddressText": "string"
				  }
				]
				
				주의:
				- placeId가 %s 와 같아야 함.
				- 지역/상호 표기가 조금 달라도 placeId 일치 시 허용.
				- menus 배열은 0개 이상 허용, 단 각 항목의 "name"은 반드시 존재/비공백.
				- price/description 은 모르면 생략하거나 null.
				- 모르는 필드는 빈 문자열("") 또는 빈 배열([]) 사용.
				""".formatted(requiredPlaceId, regionLabelClean, storeNameHint, requiredPlaceId, requiredPlaceId);

		Optional<StoreDetailResponse> strict = tryDetailOnce(strictPrompt, requiredPlaceId);
		if (strict.isPresent()) return strict;

		Optional<StoreDetailResponse> relaxed = tryDetailOnce(relaxedPrompt, requiredPlaceId);
		if (relaxed.isPresent()) return relaxed;

		Optional<StoreDetailResponse> fallback = fallbackDetailFromGrounding(requiredPlaceId);
		if (fallback.isEmpty()) {
			log.info("storeDetail: empty body for placeId={}, regionLabel={}", requiredPlaceId, request.regionLabel());
		}
		return fallback;
	}

	private Optional<StoreDetailResponse> tryDetailOnce(String prompt, String requiredPlaceId) {
		String raw = geminiService.generateWithBoth(prompt);
		try {
			JsonNode root = objectMapper.readTree(raw);
			JsonNode candidateNode = root.at("/candidates/0");
			if (candidateNode.isMissingNode()) return Optional.empty();

			String body = removeCodeFence(candidateNode.at("/content/parts/0/text").asText("")).trim();
			if (body.isBlank() || "[]".equals(body)) return Optional.empty();

			List<StoreDetailResponse> list = objectMapper.readValue(body, new TypeReference<>() {
			});
			if (list.isEmpty()) return Optional.empty();

			StoreDetailResponse first = list.get(0);
			if (!requiredPlaceId.equals(first.placeId())) return Optional.empty();


			return Optional.of(first);

		} catch (Exception e) {
			log.debug("tryDetailOnce: parse failed (ignored). cause={}", e.toString());
			return Optional.empty();
		}
	}

	private Optional<StoreDetailResponse> fallbackDetailFromGrounding(String requiredPlaceId) {
		String probe = geminiService.generateWithBoth("다음 placeId 관련 정보를 수집해 주세요: " + requiredPlaceId);
		try {
			JsonNode root = objectMapper.readTree(probe);
			for (JsonNode chunk : root.at("/candidates/0/groundingMetadata/groundingChunks")) {
				JsonNode maps = chunk.get("maps");
				if (maps == null) continue;

				String placeId = maps.path("placeId").asText("");
				if (!requiredPlaceId.equals(placeId)) continue;

				String title = maps.path("title").asText("");
				String formattedAddress = maps.path("formattedAddress").asText("");

				List<String> images = new ArrayList<>();
				for (JsonNode img : chunk.path("images")) {
					String url = img.path("url").asText("");
					if (!url.isBlank()) images.add(url);
				}

				StoreDetailResponse fallback = new StoreDetailResponse(
						placeId,          // placeId
						title,            // name
						"",               // phone
						"",               // description
						"",               // sidoName
						"",               // sigunguName
						"",               // emdName
						"",               // detail
						formattedAddress, // roadAddressText
						"",               // jibunAddressText
						List.of(),        // categories
						List.of(),        // facilities
						List.of(),        // menus
						images            // images
				);
				return Optional.of(fallback);
			}
		} catch (Exception e) {
			log.debug("fallbackDetailFromGrounding: parse failed (ignored). cause={}", e.toString());
		}
		return Optional.empty();
	}
}