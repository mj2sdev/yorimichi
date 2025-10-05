package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.ReviewDTO;
import com.jslhrd.yorimichi.exception.ReviewNotFoundException;
import com.jslhrd.yorimichi.exception.StoreNotFoundException;
import com.jslhrd.yorimichi.mapper.ReviewMapper;
import com.jslhrd.yorimichi.mapper.RootMapper;
import com.jslhrd.yorimichi.mapper.StoreMapper;
import com.jslhrd.yorimichi.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewManager implements ReviewService {

	private final RootMapper rootMapper;
	private final StoreMapper storeMapper;
	private final ReviewMapper reviewMapper;

	@Override
	public List<ReviewDTO> findAll() {
		// TODO: 무한 스크룰 및 review 상세 정보 추후 구현
		return reviewMapper.selectAll();
	}

	@Override
	public List<ReviewDTO> findAllByStoreId(Long storeId) {
		// TODO: 무한 스크룰 및 review 상세 정보 추후 구현
		return reviewMapper.selectAllByStoreId(storeId);
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

		boolean exists = storeMapper.existsActive(storeId);
		if (!exists) {
			throw new StoreNotFoundException(storeId);
		}

		review.setUserId(userId);
		review.setStoreId(storeId);

		int rootAffected = rootMapper.insert(review);
		if (rootAffected == 0 || review.getId() == null) {
			log.warn("Root insert failed or reviewId not generated: rootAffected={}, review={}", rootAffected, review);
			throw new IllegalStateException("Root insert failed or no generated reviewId");
		}

		int reviewAffected = reviewMapper.insert(review);
		if (reviewAffected == 0) {
			log.warn("Review insert failed: reviewAffected={}, review={}", reviewAffected, review);
			throw new IllegalStateException("Review insert failed");
		}

		log.info("Review created reviewId={}", review.getId());
	}

	@Override
	@Transactional
	public void update(Long userId, Long reviewId, ReviewDTO review) {

		int affected = reviewMapper.update(userId, reviewId, review);
		if (affected == 1) {
			log.info("Review updated reviewId={}", reviewId);
			return;
		}

		boolean exists = reviewMapper.existsActive(reviewId);
		if (!exists) {
			throw new ReviewNotFoundException(reviewId);
		}

		throw new AccessDeniedException("리뷰 수정 권한이 없습니다.");
	}

	@Override
	@Transactional
	public void delete(Long userId, Long reviewId) {

		int affected = reviewMapper.deleteById(userId, reviewId);
		if (affected == 1) {
			log.info("Review soft deleted reviewId={}", reviewId);
			return;
		}

		boolean exists = reviewMapper.existsActive(reviewId);
		if (!exists) {
			throw new ReviewNotFoundException(reviewId);
		}

		throw new AccessDeniedException("리뷰 삭제 권한이 없습니다.");
	}
}