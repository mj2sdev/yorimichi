package com.jslhrd.yorimichi.controller;

import org.springframework.web.bind.annotation.RestController;

import com.jslhrd.yorimichi.domain.ReportDTO;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
public class ReportController {

	//private final ReportService reportService;

	//신고하기
	@PostMapping("/report")
	public void report(@RequestBody ReportDTO report) {
		//TODO: 대상의 유형(가게/유저/리뷰/같이먹기), 대상의 id, 신고자 id,  신고 분류, 신고 내용
		//대상의 유형이 DTO에 없음.
		//서비스로 보내야함.
	}
	
	
}
