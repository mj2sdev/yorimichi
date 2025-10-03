package com.jslhrd.yorimichi.controller;

import org.springframework.web.bind.annotation.RestController;

import com.jslhrd.yorimichi.domain.ReportDTO;
import com.jslhrd.yorimichi.service.ReportService;

import lombok.RequiredArgsConstructor;

import java.security.Principal;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequiredArgsConstructor
public class ReportController {

	private final ReportService reportService;

	//신고하기
	@PostMapping("/report/{category}/{rootId}")
	public void submitReport(@RequestBody ReportDTO report, @PathVariable("category") String category, @PathVariable("rootId") long rootId, Principal principal) {
		//TODO: 대상의 유형(가게/유저/리뷰/같이먹기), 대상의 id, 신고자 id,  신고 분류, 신고 내용
		//TODO: userId 가져오기.
		long userId = 0;
		reportService.saveReport(category, rootId, userId, report);
	}
	
	@PutMapping("/report/{reportId}")
	public void approveReport(@PathVariable("reportId") Long reportId){
		reportService.putReport(reportId);
	}

	@DeleteMapping("/report{reportId}")
	public void rejectReport(@PathVariable("reportId") Long reportId){
		reportService.deleteReport(reportId);
	}
}
