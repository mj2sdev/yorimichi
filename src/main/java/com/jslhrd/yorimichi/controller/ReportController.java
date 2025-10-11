package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.domain.ReportDTO;
import com.jslhrd.yorimichi.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class ReportController {

	private final ReportService reportService;

	@GetMapping("/reports")
	public List<ReportDTO> listReport() {
		return reportService.findAll();
	}

	//신고하기
	@PostMapping("/report/{rootId}")
	public void createReport(
			@AuthenticationPrincipal(expression = "userId") Long userId,
			@PathVariable("rootId") Long rootId,
			@RequestBody ReportDTO report
	) {
		reportService.save(userId, rootId, report);
	}

	@PatchMapping("/report/{reportId}")
	public void updateReport(
			@PathVariable("reportId") Long reportId,
			@RequestBody ReportDTO report
	) {
		reportService.updateStatus(reportId, report);
	}

	// TODO: update 에서 모두 처리하는건 어떤가요?
	@DeleteMapping("/report{reportId}")
	public void cancelReport(
			@AuthenticationPrincipal(expression = "userId") Long userId,
			@PathVariable("reportId") Long reportId) {
		reportService.cancel(userId, reportId);
	}
}