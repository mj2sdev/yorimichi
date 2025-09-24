package com.jslhrd.yorimichi.controller;

import org.springframework.web.bind.annotation.RestController;

import com.jslhrd.yorimichi.domain.ReviewDTO;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
public class ReviewController {
	
	//리뷰상세
	@GetMapping("/review/{id}")
	public void showReview(@PathVariable Long id) {
		//서비스에서 ReviewDTO 받아와서 리턴타입 고쳐야함
		//return ReviewDTO review;
	}

	//리뷰작성
	@PostMapping("/review")
	public void submitReview(@RequestBody ReviewDTO review ) {
		//TODO: process POST request
		//서비스로 review 보내기
		//return List<ReviewDTO> reviews
	}
	
	//리뷰수정
	@PutMapping("/review")
	public void updateReview(@RequestBody ReviewDTO review) {
		//서비스로 보내서 수정
	}

	//리뷰삭제
	@DeleteMapping("/review")
	public void deleteReview(@RequestBody Long review){
		//서비스로 보내서 삭제
		//return List<ReviewDTO> reviews
	}
	
}
