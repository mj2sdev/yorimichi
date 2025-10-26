package com.jslhrd.yorimichi.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	public List<ReviewDTO> showReviewlatest() {
		return reviewService.findAll();
	}

	//특정 가게 리뷰 리스트
	@GetMapping("/stores/{storeId}/reviews")
	public List<ReviewDTO> showReviewListById(@PathVariable("storeId") Long storeId) {
		return reviewService.findAllByStoreId(storeId);
	}

	//리뷰상세
	@GetMapping("/review/{reviewId}")
	public ReviewDTO showReview(@PathVariable("reviewId") Long reviewId) {
		return reviewService.findById(reviewId);
	}


	// 리뷰작성
	@PostMapping("/store/{storeId}/review")
	public void submitReview(
			@PathVariable("storeId") Long storeId,
			@AuthenticationPrincipal(expression = "userId") Long userId,
			@ModelAttribute ReviewDTO dto) {
		reviewService.save(userId, storeId, dto);
	}

	//리뷰수정
	@PutMapping("/review/{reviewId}")
	public void updateReview(
			@PathVariable("reviewId") Long reviewId,
			@AuthenticationPrincipal(expression = "userId") Long userId,
			@RequestBody ReviewDTO dto
	) {
		reviewService.update(reviewId, userId, dto);

	}

	//리뷰삭제
	@DeleteMapping("/review/{reviewId}")
	public void deleteReview(
			@PathVariable("reviewId") Long reviewId,
			@AuthenticationPrincipal(expression = "userId") Long userId
	) {
		System.out.println(reviewId);
		reviewService.delete(userId, reviewId);
	}

	@PatchMapping("/review/{reviewId}")
	public void restoreReview(
		@PathVariable("reviewId") Long reviewId,
		@AuthenticationPrincipal(expression = "userId") Long userId
	){
		System.out.println(reviewId);
		reviewService.restore(userId, reviewId);
	}

}
