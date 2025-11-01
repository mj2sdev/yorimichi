package com.jslhrd.yorimichi.gemini;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jslhrd.yorimichi.domain.RegionEmdDTO;
import com.jslhrd.yorimichi.domain.RegionSidoDTO;
import com.jslhrd.yorimichi.domain.RegionSigunguDTO;
import com.jslhrd.yorimichi.enums.ToolMode;
import com.jslhrd.yorimichi.gemini.dto.request.RegionStoreRequest;
import com.jslhrd.yorimichi.gemini.dto.response.StoreNameRegionResponse;
import com.jslhrd.yorimichi.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AdminService {

	private static final Pattern CID_PATTERN = Pattern.compile("[?&]cid=(\\d+)");

	private final RegionService regionService;
	private final GeminiService geminiService;
	private final ObjectMapper objectMapper;

	private static String buildPrompt(String region, int count) {
		return """
				한국어로만 작성.
				
				아래 지역에서 현지 평이 좋은 맛집을 **정확히 %d곳** 선정해 주세요.
				
				출력 규칙:
				- 출력은 JSON 배열 리터럴 **하나만**. 앞뒤 공백/문장/주석/코드펜스(``` 등) 절대 금지.
				- 배열 길이는 **정확히 %d**.
				- 각 요소 객체는 **아래 5개 키만** 포함(철자·대소문자·순서 엄수), null/빈 문자열 금지:
				  {
				    "storeName": "string",
				    "sidoName": "string",
				    "sigunguName": "string",
				    "emdName": "string",
				    "placeId": "string"
				  }
				- **placeId는 반드시 Google Place ID**만 사용 (형식: "places/ChIJ...").
				  숫자 CID(예: 9009..., 48...) 또는 "maps.google.com/?cid=..."는 절대 금지.
				- 행정명칭은 대한민국 공식 표기(시/도, 시/군/구, 읍/면/동) 사용.
				- 요청 지역 내부 장소만 선정, **중복 placeId 금지**.
				- 조건을 만족할 수 없으면 빈 배열([]) 반환.
				
				요청 지역:
				- %s
				""".formatted(count, count, region);
	}

	private static String stripCodeFence(String s) {
		if (s == null) return "";
		String t = s.trim();
		if (t.startsWith("```")) {
			int nl = t.indexOf('\n');
			if (nl > 0) t = t.substring(nl + 1);
			int end = t.lastIndexOf("```");
			if (end >= 0) t = t.substring(0, end);
		}
		return t.trim();
	}

	private static String extractCid(String uri) {
		if (uri == null) return null;
		Matcher m = CID_PATTERN.matcher(uri);
		return m.find() ? m.group(1) : null;
	}

	private static String normalizePlaceId(String placeId,
	                                       String title,
	                                       Map<String, String> cidToPlaceId,
	                                       Map<String, String> titleToPlaceId) {
		String pid = placeId;
		if (pid != null && pid.matches("\\d+")) {                 // 숫자면 CID로 간주
			pid = cidToPlaceId.getOrDefault(pid, pid);
		}
		if (pid == null || !pid.startsWith("places/")) {           // 여전히 비정상이면 title 매핑 시도
			String byTitle = titleToPlaceId.get(title);
			if (byTitle != null) pid = byTitle;
		}
		return pid;
	}

	private static String joinNonBlank(String sep, String... parts) {
		return Arrays.stream(parts)
				.filter(Objects::nonNull)
				.map(String::trim)
				.filter(s -> !s.isBlank())
				.reduce((a, b) -> a + sep + b)
				.orElse("");
	}

	public List<StoreNameRegionResponse> findStores(RegionStoreRequest request) {
		String regionLabel = resolveRegionLabel(request);            // 1) 지역명 문자열
		int count = Math.max(1, Math.min(20, request.count()));     // 1~20 클램핑
		String prompt = buildPrompt(regionLabel, count);         // 2) 프롬프트

		String raw = geminiService.generate(prompt, ToolMode.BOTH, null, null); // 3) 호출

		try {
			JsonNode root = objectMapper.readTree(raw);

			String jsonArrayText = stripCodeFence(
					root.at("/candidates/0/content/parts/0/text").asText("")
			);
			if (jsonArrayText.isBlank()) return List.of();

			// 4) 1차 파싱
			List<StoreNameRegionResponse> items = objectMapper.readValue(
					jsonArrayText, new TypeReference<>() {
					}
			);

			// 5) grounding으로 placeId 보정 맵 구성
			Map<String, String> cidToPlaceId = new HashMap<>();
			Map<String, String> titleToPlaceId = new HashMap<>();
			for (JsonNode chunk : root.at("/candidates/0/groundingMetadata/groundingChunks")) {
				JsonNode maps = chunk.get("maps");
				if (maps == null) continue;
				String uri = maps.path("uri").asText("");
				String pid = maps.path("placeId").asText("");
				String title = maps.path("title").asText("");

				String cid = extractCid(uri);
				if (cid != null && pid.startsWith("places/")) cidToPlaceId.put(cid, pid);
				if (!title.isBlank() && pid.startsWith("places/")) titleToPlaceId.put(title, pid);
			}

			// 6) 최종 정규화 + 필터링
			List<StoreNameRegionResponse> fixed = new ArrayList<>(items.size());
			for (StoreNameRegionResponse it : items) {
				String pid = normalizePlaceId(it.placeId(), it.storeName(), cidToPlaceId, titleToPlaceId);
				if (pid != null && pid.startsWith("places/")) {
					fixed.add(new StoreNameRegionResponse(
							it.storeName(), it.sidoName(), it.sigunguName(), it.emdName(), pid
					));
				}
			}

			// 혹시 모델이 과다 반환했어도 count까지만 보장
			return fixed.size() > count ? fixed.subList(0, count) : fixed;

		} catch (Exception e) {
			return List.of(); // 필요 시 로깅
		}
	}

	private String resolveRegionLabel(RegionStoreRequest request) {
		RegionSidoDTO sido = regionService.findBySidoId(request.sidoId());
		if (sido == null) throw new IllegalArgumentException("유효하지 않은 sidoId");

		String sidoName = sido.getName();
		String sigunguName = null;
		String emdName = null;

		if (request.emdId() != null) {
			RegionEmdDTO emd = regionService.findByEmdId(request.emdId());
			if (emd == null) throw new IllegalArgumentException("유효하지 않은 emdId");
			emdName = emd.getName();

			// sigunguId가 비었으면 emd의 상위로 보정
			if (request.sigunguId() == null) {
				RegionSigunguDTO parent = regionService.findBySigunguId(emd.getSigunguId());
				sigunguName = parent != null ? parent.getName() : null;
			}
		}

		if (request.sigunguId() != null) {
			RegionSigunguDTO sigungu = regionService.findBySigunguId(request.sigunguId());
			if (sigungu == null || !Objects.equals(sigungu.getSidoId(), request.sidoId())) {
				throw new IllegalArgumentException("sidoId/sigunguId 불일치");
			}
			sigunguName = sigungu.getName();
		}

		return joinNonBlank(" ", sidoName, sigunguName, emdName);
	}
}