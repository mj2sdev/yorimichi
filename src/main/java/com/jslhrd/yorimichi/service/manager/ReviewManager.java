package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.ImageDTO;
import com.jslhrd.yorimichi.domain.ReviewDTO;
import com.jslhrd.yorimichi.domain.ReviewFoodDTO;
import com.jslhrd.yorimichi.exception.*;
import com.jslhrd.yorimichi.mapper.ReviewFoodMapper;
import com.jslhrd.yorimichi.mapper.ReviewMapper;
import com.jslhrd.yorimichi.mapper.RootImageMapper;
import com.jslhrd.yorimichi.mapper.RootMapper;
import com.jslhrd.yorimichi.mapper.StoreMapper;
import com.jslhrd.yorimichi.mapper.UserMapper;
import com.jslhrd.yorimichi.service.GoogleDriveService;
import com.jslhrd.yorimichi.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

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
	public void restore(Long userId, Long reviewId){
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
}