package com.jslhrd.yorimichi.gemini;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jslhrd.yorimichi.domain.RegionEmdDTO;
import com.jslhrd.yorimichi.domain.RegionSidoDTO;
import com.jslhrd.yorimichi.domain.RegionSigunguDTO;
import com.jslhrd.yorimichi.gemini.store.dto.RegionNames;
import com.jslhrd.yorimichi.gemini.store.dto.request.RegionStoreRequest;
import com.jslhrd.yorimichi.gemini.store.dto.request.StoreDetailRequest;
import com.jslhrd.yorimichi.gemini.store.dto.response.StoreDetailResponse;
import com.jslhrd.yorimichi.gemini.store.dto.response.StoreNameRegionResponse;
import com.jslhrd.yorimichi.service.RegionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

	private static final String JAPANESE_RULES = """
			日本語のみを使用してください。英語やその他の言語は禁止です。
			次のいずれかを満たせない場合は空配列([])を返してください。
			- 説明・カテゴリ・設備・住所は日本語表記（固有名詞は原文可）
			- 英語原文がある場合は日本語に意訳し、原文は省略
			- 出力は JSON 配列リテラルのみ（前後のテキスト／コードフェンス／コメント禁止）
			- 日本語表記のみを使用（店名などの固有名詞は原文可）
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

		String prompt = JAPANESE_RULES + """
				
				以下の地域から、現地評価の高い飲食店をちょうど %d 件、厳選してください。
				
				出力ルール:
				- 出力は JSON 配列リテラルのみ。前後の空白・文章・コメント・コードフェンス(```)は禁止。
				- 配列の長さは正確に %d。
				- 各要素オブジェクトは次の 5 キーのみを含むこと（綴り・大文字小文字・順番を厳守）。null や空文字は不可:
				  {
				    "storeName": "string",
				    "sidoName": "string",
				    "sigunguName": "string",
				    "emdName": "string",
				    "placeId": "string"
				  }
				- placeId は必ず Google Place ID のみを使用（形式: "places/ChIJ..."）。
				- 行政名称は大韓民国の公的表記（市/道、市/郡/区、邑/面/洞）に従うこと。
				- 要求地域の内部にある店舗のみを選定し、placeId の重複は禁止。
				- 条件を満たせない場合は空配列([])を返すこと。
				
				要求地域:
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
		String regionLabelClean = request.regionLabel();
		String storeNameHint = request.storeName();

		String strictPrompt = JAPANESE_RULES + """
				
				次の条件をすべて満たす店舗 1 件の詳細情報のみを JSON 配列で返してください。
				- 地域（行政名）: %s
				- 店名（検索ヒント）: %s
				- 次の placeId と完全一致であること: %s   （形式: "places/ChIJ..."）
				
				出力ルール（厳格）:
				- 出力は JSON 配列リテラルのみ。前後の空白・文章・コメント・コードフェンス(```)は禁止。
				- 配列の長さは正確に 1。不確実な場合は空配列([])。
				- キー／順序／綴りは厳守。null は最小限（不明な場合のみ空文字・空配列を使用）。
				- 可能であれば実際の販売メニュー名を 3〜8 件収集して "menus" に格納。価格・説明が不確実なら省略し、名前のみを入れてよい。
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
				    "detail": "string",             // 詳細住所
				    "placeId": "string",            // 必ず %s と完全一致
				    "roadAddressText": "string",
				    "jibunAddressText": "string"
				  }
				]
				
				注意:
				- placeId が %s と異なる場合は必ず空配列([])。
				- 地域／店名が一致しない場合も空配列([])。
				- menus 配列は 0 件以上可。ただし各要素の "name" は必須かつ空白不可。
				- price / description は不明なら省略または null。
				- 不明なフィールドは空文字("") または空配列([])を使用。
				""".formatted(regionLabelClean, storeNameHint, requiredPlaceId, requiredPlaceId, requiredPlaceId);

		String relaxedPrompt = JAPANESE_RULES + """
				
				次の placeId に該当する店舗 1 件の詳細情報を JSON 配列で返してください。
				- placeId（必須）: %s
				- 地域（参考）: %s
				- 店名（ヒント）: %s
				
				出力ルール:
				- 出力は JSON 配列リテラル 1 つ、長さは 1（不確実なら空配列 []）。
				- 各オブジェクトは以下のフラットなキーのみを使用:
				- 可能であれば実際の販売メニュー名を 3〜8 件収集して "menus" に格納。価格・説明が不確実なら省略し、名前のみで可。
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
				    "placeId": "string",        // 必ず %s と同一
				    "roadAddressText": "string",
				    "jibunAddressText": "string"
				  }
				]
				
				注意:
				- placeId は %s と同一でなければならない。
				- 地域／店名の表記が多少異なっても、placeId が一致すれば許容。
				- menus 配列は 0 件以上可。ただし各要素の "name" は必須かつ空白不可。
				- price / description は不明なら省略または null。
				- 不明なフィールドは空文字("") または空配列([])を使用。
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