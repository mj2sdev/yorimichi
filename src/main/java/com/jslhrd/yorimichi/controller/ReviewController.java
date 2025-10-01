package com.jslhrd.yorimichi.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jslhrd.yorimichi.domain.ReviewDTO;
import com.jslhrd.yorimichi.service.ReviewService;

import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
@RequiredArgsConstructor
public class ReviewController {
	
	private final ReviewService reviewService;


	//리뷰상세
	@GetMapping("/review")
	public ReviewDTO showReview(@RequestParam(value = "id") Long reviewId) {
		return reviewService.findById(reviewId);
	}

	//특정 가게 리뷰 리스트
	@GetMapping("/reviews")
	public List<ReviewDTO> showReviewListById(@RequestParam(value = "id") Long StoreId) {
		return reviewService.findAllByStoreId(StoreId);
	}

	//리뷰 리스트
	@GetMapping("/reviewsall")
	public List<ReviewDTO> showReviewlatest() {
		return reviewService.findAll();
	}
	

	//리뷰작성
	@PostMapping("/review")
	public void submitReview(@RequestBody MultipartFile review ) {
		reviewService.save(review);
	}
	
	//리뷰수정
	@PutMapping("/review")
	public void updateReview(@RequestBody MultipartFile review) {
		reviewService.update(review);
	}

	//리뷰삭제
	@DeleteMapping("/review")
	public void deleteReview(@RequestBody ReviewDTO review, Principal principal){
		reviewService.delete(review,principal);
	}
	
}
