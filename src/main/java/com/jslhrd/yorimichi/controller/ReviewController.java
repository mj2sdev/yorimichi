package com.jslhrd.yorimichi.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jslhrd.yorimichi.domain.ReviewDTO;
import com.jslhrd.yorimichi.service.ReviewService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ReviewController {

	private final ReviewService reviewService;

	//리뷰 리스트
	@GetMapping("/reviews")
	public List<ReviewDTO> listReview() {
		return reviewService.findAll();
	}

	//특정 가게 리뷰 리스트
	@GetMapping("/stores/{storeId}/reviews")
	public List<ReviewDTO> listReviewByStore(@PathVariable("storeId") Long storeId) {
		return reviewService.findAllByStoreId(storeId);
	}

	//리뷰상세
	@GetMapping("/review/{reviewId}")
	public ReviewDTO getReview(@PathVariable("reviewId") Long reviewId) {
		return reviewService.findById(reviewId);
	}


	// 리뷰작성
	// TODO: CustomUserPrincipal 구현 필요.
	@PostMapping("/stores/{storeId}/review")
	public void createReview(
			@AuthenticationPrincipal(expression = "userId") Long userId,
			@PathVariable("storeId") Long storeId,
			@RequestBody ReviewDTO review
	) {
		reviewService.save(userId, storeId, review);
	}

	//리뷰수정
	// TODO: CustomUserPrincipal 구현 필요.
	@PatchMapping("/review/{reviewId}")
	public void updateReview(
			@PathVariable("reviewId") Long reviewId,
			@AuthenticationPrincipal(expression = "userId") Long userId,
			@RequestBody ReviewDTO review
	) {
		reviewService.update(userId, reviewId, review);

	}

	//리뷰삭제
	// TODO: CustomUserPrincipal 구현 필요.
	@DeleteMapping("/review/{reviewId}")
	public void deleteReview(
			@AuthenticationPrincipal(expression = "userId") Long userId,
			@PathVariable("reviewId") Long reviewId
	) {
		reviewService.delete(userId, reviewId);
	}

}