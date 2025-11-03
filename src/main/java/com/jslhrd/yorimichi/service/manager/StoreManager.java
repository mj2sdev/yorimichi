package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.CategoryDTO;
import com.jslhrd.yorimichi.domain.SearchDTO;
import com.jslhrd.yorimichi.domain.StoreDTO;
import com.jslhrd.yorimichi.exception.AddressNotFoundException;
import com.jslhrd.yorimichi.exception.BadRequestException;
import com.jslhrd.yorimichi.exception.DuplicateStoreException;
import com.jslhrd.yorimichi.exception.StoreNotFoundException;
import com.jslhrd.yorimichi.mapper.AddressMapper;
import com.jslhrd.yorimichi.mapper.RootMapper;
import com.jslhrd.yorimichi.mapper.StoreMapper;
import com.jslhrd.yorimichi.service.StoreService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreManager implements StoreService {

	private static final double WEIGHT_AVG_RATING = 4.0;
	private static final double WEIGHT_REVIEW_TOTAL = 1.2;
	private static final double WEIGHT_REVIEW_RECENT = 1.5;
	private static final double WEIGHT_LIKE_COUNT = 0.6;
	private static final double WEIGHT_BOOKMARK_COUNT = 0.8;
	private static final double WEIGHT_OPEN_COEAT_COUNT = 1.0;

	private final RootMapper rootMapper;
	private final StoreMapper storeMapper;
	private final AddressMapper addressMapper;

	@Override
	public List<StoreDTO> findAll(SearchDTO search) {
		// TODO: 무한 스크롤 구현 후 교체
		return storeMapper.selectAll(search);
	}

	@Override
	public List<StoreDTO> findAllByUserLike(Long userId) {
		// TODO: mappers 구현 후 교체
		return Collections.emptyList();

	}

	public List<StoreDTO> findAllByRecommend(int limit) {

		List<StoreDTO> stores = storeMapper.selectRecommend(9, 30);

		return stores.stream()
				// 1) 1차 정렬 키: 가중치 점수(높을수록 우선)
				//    - this::scoreOf 는 평균/총리뷰/최근리뷰/좋아요/북마크/코잇을 ln 포화로 가중합
				//    - comparingDouble 은 double 키 추출 Comparator
				.sorted(Comparator.comparingDouble(this::scoreOf)
						// 1-1) 내림차순(점수 큰 순서)으로 뒤집기
						.reversed()

						// 2) 2차 정렬 키: 최근 리뷰 수(recentCount) — 많을수록 우선
						//    - review 가 null일 수도 있어 NPE 방지를 위해 삼항 연산자로 0 대체
						//    - reverseOrder() 로 내림차순
						.thenComparing(
								s -> s.getReview() != null ? s.getReview().getRecentCount() : 0,
								Comparator.reverseOrder()
						)

						// 3) 3차 정렬 키: 총 리뷰 수(reviewCount) — 많을수록 우선
						//    - 마찬가지로 null 안전 처리
						.thenComparing(
								s -> s.getReview() != null ? s.getReview().getReviewCount() : 0,
								Comparator.reverseOrder()
						)

						// 4) 4차 정렬 키: store id — 큰 id 우선(동점 안정화용)
						//    - id가 Long 이므로 메서드 레퍼런스 사용
						//    - reverseOrder() 으로 내림차순
						.thenComparing(StoreDTO::getId, Comparator.reverseOrder())
				)

				// 5) 상위 size 개만 유지
				.limit(limit)

				// 6) 최종 리스트 생성
				.toList();
	}


	@Override
	public StoreDTO findById(Long storeId, Long userId) {
		return storeMapper.selectById(storeId, userId)
				.orElseThrow(() -> new StoreNotFoundException(storeId));
	}

	@Override
	@Transactional
	public void save(StoreDTO store) {

		assertActiveAddress(store.getAddressId());

		rootMapper.insert(store);
		if (store.getId() == null) {
			throw new IllegalStateException("Root: insert failed or no generated storeId");
		}

		try {
			storeMapper.insert(store);
		} catch (DuplicateKeyException e) {
			throw new DuplicateStoreException(store.getAddressId(), store.getName());
		}

		log.info("Store: created storeId={}", store.getId());
	}

	@Override
	@Transactional
	public void update(Long storeId, StoreDTO store) {

		if (store.getId() != null && !storeId.equals(store.getId())) {
			throw new BadRequestException("경로의 storeId 와 본문의 id 가 다릅니다.");
		}

		if (store.getAddressId() != null) {
			assertActiveAddress(store.getAddressId());
		}

		try {
			boolean affected = storeMapper.update(storeId, store) > 0;
			if (!affected) {
				assertActiveStore(storeId);
				log.debug("Store: update no-op storeId={}, store={}", storeId, store);
				return;
			}
		} catch (DuplicateKeyException e) {
			throw new DuplicateStoreException(store.getAddressId(), store.getName());
		}

		log.info("Store: updated storeId={}", storeId);
	}

	@Override
	@Transactional
	public void delete(Long storeId) {

		boolean affected = storeMapper.deleteById(storeId) > 0;
		if (!affected) {
			assertActiveStore(storeId);
			log.debug("Store: delete no-op storeId={}", storeId);
			return;
		}

		log.info("Store: soft deleted storeId={}", storeId);
	}

	private void assertActiveAddress(Long addressId) {
		boolean exists = addressMapper.existsById(addressId);
		if (!exists) {
			throw new AddressNotFoundException(addressId);
		}
	}

	private void assertActiveStore(Long storeId) {
		boolean exists = storeMapper.existsActive(storeId);
		if (!exists) {
			throw new StoreNotFoundException(storeId);
		}
	}

	double scoreOf(StoreDTO store) {

		double avg = (store.getReview() != null) && (store.getReview().getAvgRating() != null) ? store.getReview().getAvgRating() : 0.0;
		int total = (store.getReview() != null) && (store.getReview().getReviewCount() != null) ? store.getReview().getReviewCount() : 0;
		int recent = (store.getReview() != null) && (store.getReview().getRecentCount() != null) ? store.getReview().getRecentCount() : 0;
		int likeCnt = (store.getLike() != null) && (store.getLike().getCount() != null) ? store.getLike().getCount() : 0;
		int bmCnt = (store.getBookmark() != null) && (store.getBookmark().getCount() != null) ? store.getBookmark().getCount() : 0;
		int openCnt = (store.getCoeat() != null) && (store.getCoeat().getCommentCount() != null) ? store.getCoeat().getCommentCount() : 0;

		return
				WEIGHT_AVG_RATING * (avg / 5.0)
						+ WEIGHT_REVIEW_TOTAL * Math.log1p(total)
						+ WEIGHT_REVIEW_RECENT * Math.log1p(recent)
						+ WEIGHT_LIKE_COUNT * Math.log1p(likeCnt)
						+ WEIGHT_BOOKMARK_COUNT * Math.log1p(bmCnt)
						+ WEIGHT_OPEN_COEAT_COUNT * Math.log1p(openCnt);
	}
}