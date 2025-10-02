package com.jslhrd.yorimichi.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.jslhrd.yorimichi.domain.ReviewDTO;
import com.jslhrd.yorimichi.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

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



	// 리뷰작성
	// TODO: CustomUserPrincipal 구현 필요.
	@PostMapping("/stores/{storeId}/review")
	public void submitReview(
			@PathVariable Long storeId,
			
			@AuthenticationPrincipal(expression = "userId") Long userId,
			@RequestBody ReviewDTO dto
	) {
		reviewService.save(userId, storeId, dto);
	}

	//리뷰수정
	@PutMapping("/review/{reviewId}")
	public void updateReview(
			@PathVariable Long reviewId,
			// TODO: CustomUserPrincipal 구현 필요.
			@AuthenticationPrincipal(expression = "userId") Long userId,
			@RequestBody ReviewDTO dto
	) {
		reviewService.update(reviewId, userId, dto);

	}

	//리뷰삭제
	@DeleteMapping("/review/{reviewId}")
	public void deleteReview(
			@PathVariable Long reviewId,
			// TODO: CustomUserPrincipal 구현 필요.
			@AuthenticationPrincipal(expression = "userId") Long userId
	) {
		reviewService.delete(reviewId, userId);
	}

}
