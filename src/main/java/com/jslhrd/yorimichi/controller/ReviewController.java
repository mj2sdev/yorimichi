package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.domain.ReviewDTO;
import com.jslhrd.yorimichi.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	//리뷰 리스트
	@GetMapping("/reviews")
	public List<ReviewDTO> showReviewlatest() {
		return reviewService.findAll();
	}

	//특정 가게 리뷰 리스트
	@GetMapping("/stores/{storeId}/reviews")
	public List<ReviewDTO> showReviewListById(@PathVariable("storeId") Long storeId) {
		return reviewService.findAllByStoreId(storeId);
	}

	//리뷰상세
	@GetMapping("/reviews/{reviewId}")
	public ReviewDTO showReview(@PathVariable("reviewId") Long reviewId) {
		return reviewService.findById(reviewId);
	}


	//리뷰작성
	@PostMapping("/stores/{storeId}/reviews")
	public void submitReview(
			@PathVariable Long storeId,
			// TODO: CustomUserPrincipal 구현 필요.
			@AuthenticationPrincipal(expression = "userId") Long userId,
			@RequestBody ReviewDTO dto
	) {
		reviewService.save(userId, storeId, dto);
	}

	//리뷰수정
	@PutMapping("/reviews/{reviewId}")
	public void updateReview(
			@PathVariable Long reviewId,
			// TODO: CustomUserPrincipal 구현 필요.
			@AuthenticationPrincipal(expression = "userId") Long userId,
			@RequestBody ReviewDTO dto
	) {
		reviewService.update(reviewId, userId, dto);
	}

	//리뷰삭제
	@DeleteMapping("/reviews/{reviewId}")
	public void deleteReview(
			@PathVariable Long reviewId,
			// TODO: CustomUserPrincipal 구현 필요.
			@AuthenticationPrincipal(expression = "userId") Long userId
	) {
		reviewService.delete(reviewId, userId);
	}

}