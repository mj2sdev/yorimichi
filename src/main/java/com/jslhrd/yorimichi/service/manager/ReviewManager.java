package com.jslhrd.yorimichi.service.manager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jslhrd.yorimichi.domain.ImageDTO;
import com.jslhrd.yorimichi.domain.KeywordDTO;
import com.jslhrd.yorimichi.domain.ReviewDTO;
import com.jslhrd.yorimichi.domain.ReviewFoodDTO;
import com.jslhrd.yorimichi.exception.*;
import com.jslhrd.yorimichi.gemini.GeminiService;
import com.jslhrd.yorimichi.gemini.review.request.ReviewKeywordExtractRequest;
import com.jslhrd.yorimichi.gemini.review.response.ReviewKeywordExtractResponse;
import com.jslhrd.yorimichi.gemini.review.response.ReviewSummaryRefreshResponse;
import com.jslhrd.yorimichi.mapper.*;
import com.jslhrd.yorimichi.service.GoogleDriveService;
import com.jslhrd.yorimichi.service.KeywordService;
import com.jslhrd.yorimichi.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewManager implements ReviewService {

	private final RootMapper rootMapper;
	private final StoreMapper storeMapper;
	private final UserMapper userMapper;
	private final ReviewMapper reviewMapper;
	private final ImageManager imageManager;
	private final RootImageMapper rootImageMapper;
	private final ReviewFoodMapper reviewFoodMapper;

	private final GoogleDriveService googleDriveService;
	private final GeminiService geminiService;
	private final KeywordService keywordService;
	private final ObjectMapper objectMapper;

	// 기본 파라미터
	private int hotWindowDays = 14;   // 최근 N일
	private int hotBatchSize = 20;  // 매일 핫처리 개수
	private int backlogBatchSize = 50;  // 매일 백로그 처리 개수
	private int buckets = 30;  // 백로그 버킷 수

	private int perStoreReviews = 20;  // 가게당 읽을 리뷰 수
	private int candidateLimit = 10;  // 한 번에 최대 처리 가게 수

	@Transactional
	public ReviewKeywordExtractResponse extractKeywords(ReviewKeywordExtractRequest request) {

		int limit = (request != null && request.limitStores() != null && request.limitStores() > 0)
				? request.limitStores()
				: candidateLimit;

		int perReviews = (request != null && request.perStoreReviews() != null && request.perStoreReviews() > 0)
				? request.perStoreReviews()
				: this.perStoreReviews;

		String mode = (request != null && request.mode() != null) ? request.mode() : "all";
		Integer days = (request != null) ? request.days() : null;
		Integer topN = (request != null) ? request.topN() : null;

		// 1) 후보 가게 조회 (리뷰가 있는 상점만)
		List<Long> storeIds = reviewMapper.selectStoreIdsByDays(days, limit);

		int processedStores = 0;
		int extractedKeywords = 0;
		int createdKeywords = 0;
		int linkedPairs = 0;
		int skippedStores = 0;
		int errors = 0;
		List<Long> updatedStoreIds = new ArrayList<>();

		for (Long storeId : storeIds) {
			try {
				// 2) 리뷰 수집
				List<String> contents = reviewMapper.selectRecentContentsByStoreId(storeId, perReviews);
				if (contents.isEmpty()) {
					skippedStores++;
					continue;
				}

				// 3) 키워드 추출(LLM)
				String prompt = buildJaKeywordPrompt(contents);
				String raw = geminiService.generateWithSearch(prompt);
				List<String> keywords = parseKeywordsFromGemini(raw);
				if (keywords.isEmpty()) {
					skippedStores++;
					continue;
				}

				// 중복 제거 + 상위 N 제한(옵션)
				LinkedHashSet<String> dedup = new LinkedHashSet<>();
				for (String k : keywords) {
					String t = k == null ? "" : k.trim();
					if (!t.isEmpty()) dedup.add(t);
				}
				List<String> finalKeywords = new ArrayList<>(dedup);
				if (topN != null && topN > 0 && finalKeywords.size() > topN) {
					finalKeywords = finalKeywords.subList(0, topN);
				}
				extractedKeywords += finalKeywords.size();

				// 4) 키워드 upsert + 링크
				int createdHere = 0;
				int linkedHere = 0;

				for (String name : finalKeywords) {
					// 키워드 id 확보 (없으면 생성, 있으면 기존 id)
					KeywordDTO dto = new KeywordDTO(name);
					keywordService.save(dto);
					Long keywordId = dto.getId();
					createdHere++;

					// store.id == root.id 구조 → storeId 그대로 사용 가능
					keywordService.addKeywordToRoot(storeId, keywordId);
					linkedHere++; // KeywordManager 가 중복 링크는 no-op 처리
				}

				createdKeywords += createdHere;
				linkedPairs += linkedHere;

				processedStores++;
				updatedStoreIds.add(storeId);

			} catch (Exception e) {
				errors++;
				log.warn("keyword refresh failed storeId={}, cause={}", storeId, e.toString());
			}
		}

		return new ReviewKeywordExtractResponse(
				processedStores,
				extractedKeywords,
				createdKeywords,
				linkedPairs,
				skippedStores,
				errors,
				updatedStoreIds
		);
	}

	private String buildJaKeywordPrompt(List<String> contents) {
		String joined = contents.stream()
				.map(s -> s.replace('\n', ' ').trim())
				.filter(s -> !s.isBlank())
				.collect(Collectors.joining("\n・"));

		return """
				以下のレビュー本文から、日本語のキーワードを抽出してください。
				ルール:
				- 出力は JSON 配列リテラルのみ（前後の文章・コードフェンス禁止）
				- 各要素は短い名詞または名詞句（最大10件）
				- 宣伝語や固有店名は除外、料理名・味・量・価格・雰囲気・接客など実体を表す語を優先
				- 似た語は代表1つに統一
				例: ["味","量","価格","雰囲気","接客","キムチ","サムギョプサル"]
				
				【レビュー】
				・%s
				""".formatted(joined);
	}

	/**
	 * LLM 응답 → 키워드 문자열 배열 파싱
	 */
	private List<String> parseKeywordsFromGemini(String raw) {
		try {
			JsonNode root = objectMapper.readTree(raw);
			String text = Optional.ofNullable(root.at("/candidates/0/content/parts/0/text").asText(null))
					.map(String::trim).orElse("");
			// ```json … ``` 제거
			if (text.startsWith("```")) {
				int i = text.indexOf('\n');
				if (i > 0) text = text.substring(i + 1);
				int j = text.lastIndexOf("```");
				if (j >= 0) text = text.substring(0, j);
				text = text.trim();
			}
			if (text.isBlank() || "[]".equals(text)) return List.of();

			JsonNode arr = objectMapper.readTree(text);
			if (!arr.isArray()) return List.of();

			List<String> out = new ArrayList<>();
			for (JsonNode n : arr) {
				if (n.isTextual()) {
					String k = n.asText("").trim();
					if (!k.isEmpty()) {
						if (k.length() > 20) k = k.substring(0, 20); // 과도하게 긴 키워드 컷
						out.add(k);
					}
				}
			}
			return out;
		} catch (Exception e) {
			log.debug("parseKeywordsFromGemini fail: {}", e.toString());
			return List.of();
		}
	}

	/**
	 * 매일 04:10 (예시)
	 */
	@Scheduled(cron = "0 10 4 * * *")
	@Transactional
	public void refreshDaily() {
		int hotDone = processHot();
		int backDone = processBacklog();
		log.info("ReviewSummary done hot={}, backlog={}", hotDone, backDone);
	}

	/**
	 * 수동 실행 (요청 크기만 덮어씀)
	 */
	@Transactional
	public ReviewSummaryRefreshResponse refreshOnce(Integer hotSize, Integer backlogSize) {
		final int hotLimit = (hotSize != null ? Math.max(hotSize, 0) : 0);
		final int backLimit = (backlogSize != null ? Math.max(backlogSize, 0) : 0);

		// 1) 후보 리스트를 "한 번만" 조회
		List<Long> hotIds = hotLimit > 0
				? reviewMapper.selectStoreIdsByDays(hotWindowDays, hotLimit)
				: List.of();

		int bucketOfToday = LocalDate.now().getDayOfYear() % buckets;
		List<Long> backlogIds = backLimit > 0
				? reviewMapper.selectBacklogStoreIdsByBucket(buckets, bucketOfToday, backLimit)
				: List.of();

		int hotCandidates = hotIds.size();
		int backlogCandidates = backlogIds.size();

		// 2) 실제 처리 (중복 제거: 핫 우선)
		LinkedHashSet<Long> union = new LinkedHashSet<>(hotIds);
		for (Long id : backlogIds) union.add(id);
		List<Long> runIds = List.copyOf(union);

		ProcessStats stats = summarizeStores(runIds);

		return new ReviewSummaryRefreshResponse(
				hotLimit,
				backLimit,
				hotCandidates,
				backlogCandidates,
				stats.updated,
				stats.skipped,
				stats.errors,
				stats.updatedStoreIds
		);
	}

	private int processHot() {
		if (hotBatchSize <= 0) return 0;
		List<Long> storeIds = reviewMapper.selectStoreIdsByDays(hotWindowDays, hotBatchSize);
		return summarizeStores(storeIds).updated; // "성공적으로 업데이트된 개수" 리턴
	}

	private int processBacklog() {
		if (backlogBatchSize <= 0) return 0;
		int bucketOfToday = LocalDate.now().getDayOfYear() % buckets;
		List<Long> storeIds = reviewMapper.selectBacklogStoreIdsByBucket(buckets, bucketOfToday, backlogBatchSize);
		return summarizeStores(storeIds).updated;
	}

	/**
	 * 요약 처리 본체
	 */
	private ProcessStats summarizeStores(List<Long> storeIds) {
		int updated = 0, skipped = 0, errors = 0;
		List<Long> updatedStoreIds = new ArrayList<>();

		for (Long storeId : storeIds) {
			try {
				List<String> contents = reviewMapper.selectRecentContentsByStoreId(storeId, perStoreReviews);

				if (contents.isEmpty()) {
					// 리뷰가 없다면 요약 공백으로 초기화(혹은 스킵 처리)
					storeMapper.updateSummaryReview(storeId, "");
					skipped++;
					continue;
				}

				String prompt = buildJaSummaryPrompt(contents);
				String raw = geminiService.generateWithSearch(prompt);
				String summary = extractPlainText(raw);
				if (summary == null || summary.isBlank()) {
					skipped++;
					continue;
				}

				storeMapper.updateSummaryReview(storeId, summary);
				updated++;
				updatedStoreIds.add(storeId);

				// 필요 시 레이트리밋
				// Thread.sleep(120);
			} catch (Exception e) {
				errors++;
				log.warn("summarize fail storeId={}, cause={}", storeId, e.toString());
			}
		}
		return new ProcessStats(updated, skipped, errors, updatedStoreIds);
	}

	private String buildJaSummaryPrompt(List<String> contents) {
		StringBuilder sb = new StringBuilder();
		sb.append("""
				以下は同一店舗の最新レビュー抜粋です。重複を避け、事実ベースで要点のみを日本語で簡潔にまとめてください。
				- 出力は日本語の箇条書き3〜5行。各行は1文、合計300文字以内。
				- 接客/味/量/価格/雰囲気/再訪意向など共通点を優先して要約。
				- 箇条書き以外の文章・前置き・コードフェンスは禁止。
				
				【レビュー本文】
				""");
		for (String c : contents) sb.append("・").append(c).append("\n");
		return sb.toString();
	}

	/**
	 * Gemini 응답에서 candidates[0].content.parts[0].text 를 안전하게 파싱
	 * (인덱스 서브스트링 방식 대신 Jackson 사용)
	 */
	private String extractPlainText(String raw) {
		if (raw == null || raw.isBlank()) return null;
		try {
			JsonNode root = objectMapper.readTree(raw);
			return root.at("/candidates/0/content/parts/0/text").asText(null);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public List<ReviewDTO> findAll() {
		// TODO: 무한 스크룰 및 review 상세 정보 추후 구현
		return reviewMapper.selectAll();
	}

	@Override
	public List<String> findContentByStoreId(Long storeId, int limit) {
		return reviewMapper.selectRecentContentsByStoreId(storeId, limit);
	}

	@Override
	public List<ReviewDTO> findAllByStoreId(Long storeId) {
		// TODO: 무한 스크룰 및 review 상세 정보 추후 구현
		return reviewMapper.selectAllByStoreId(storeId);
	}

	@Override
	public List<ReviewDTO> findAllByUserId(Long userId) {
		return List.of();
	}

	@Override
	public ReviewDTO findById(Long reviewId) {
		// TODO: 연관 DTO 조회 추구 구현
		return reviewMapper.selectById(reviewId)
				.orElseThrow(() -> new ReviewNotFoundException(reviewId));
	}

	@Override
	@Transactional
	public void save(Long userId, Long storeId, ReviewDTO review) {

		assertActiveUser(userId);
		assertActiveStore(storeId);

		review.setUserId(userId);
		review.setStoreId(storeId);

		rootMapper.insert(review);
		if (review.getId() == null) {
			throw new IllegalStateException("Root: insert failed or no generated reviewId");
		}
		reviewMapper.insert(review);

		// 이미지 처리
		List<String> imageUrls = googleDriveService.uploadFiles(review.getUploadImages());
		for (String url : imageUrls) {
			ImageDTO image = new ImageDTO();
			image.setUrl(url);
			imageManager.save(image);
			rootImageMapper.insert(review.getId(), image.getId());
		}

		// 영수증 처리 (파일이 존재할 경우)
		MultipartFile receipt = review.getUploadReceipt();
		if (!receipt.isEmpty()) {
			String url = googleDriveService.uploadFile(receipt);
			ImageDTO image = new ImageDTO();
			image.setUrl(url);
			imageManager.save(image);
			review.setReceipt(image);
		}

		// 메뉴추가
		Optional.ofNullable(review.getFoods()).ifPresent(foods -> {
			foods.stream().forEach(food -> {
				ReviewFoodDTO dto = new ReviewFoodDTO();
				dto.setFoodId(food.getId());
				dto.setReviewId(review.getId());
				reviewFoodMapper.insert(dto);
			});
		});

		log.info("Review: created reviewId={}", review.getId());
	}

	@Override
	@Transactional
	public void update(Long userId, Long reviewId, ReviewDTO review) {

		if (review.getId() != null && !reviewId.equals(review.getId())) {
			throw new BadRequestException("경로의 reviewId 와 본문의 id 가 다릅니다.");
		}

		if (review.getStoreId() != null) {
			throw new BadRequestException("리뷰 수정 시 storeId는 변경할 수 없습니다.");
		}

		boolean affected = reviewMapper.update(userId, reviewId, review) > 0;
		if (!affected) {
			assertActiveReview(reviewId);
			throw new ForbiddenException("리뷰 수정 권한이 없습니다.");
		}

		log.info("Review: updated reviewId={}", reviewId);
	}

	@Override
	@Transactional
	public void delete(Long userId, Long reviewId) {

		boolean affected = reviewMapper.deleteById(userId, reviewId) > 0;
		if (!affected) {
			assertActiveReview(reviewId);
			throw new ForbiddenException("리뷰 삭제 권한이 없습니다.");
		}

		log.info("Review: soft deleted reviewId={}", reviewId);
	}

	@Override
	@Transactional
	public void restore(Long userId, Long reviewId) {
		boolean affected = reviewMapper.restoreById(userId, reviewId) > 0;
		if (!affected) {
			assertActiveReview(reviewId);
			throw new ForbiddenException("리뷰 복구 권한이 없습니다.");
		}

		log.info("Review: soft deleted review be restored reviewId={}", reviewId);
	}

	private void assertActiveUser(Long userId) {
		boolean exists = userMapper.existsActive(userId);
		if (!exists) {
			throw new UserNotFoundException(userId);
		}
	}

	private void assertActiveStore(Long storeId) {
		boolean exists = storeMapper.existsActive(storeId);
		if (!exists) {
			throw new StoreNotFoundException(storeId);
		}
	}

	private void assertActiveReview(Long reviewId) {
		boolean exists = reviewMapper.existsActive(reviewId);
		if (!exists) {
			throw new ReviewNotFoundException(reviewId);
		}
	}

	// 간단한 처리 누적용 DTO
	private record ProcessStats(int updated, int skipped, int errors, List<Long> updatedStoreIds) {
	}
}