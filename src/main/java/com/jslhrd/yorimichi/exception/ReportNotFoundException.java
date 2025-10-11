package com.jslhrd.yorimichi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReportNotFoundException extends DomainException {

	public ReportNotFoundException(Long reportId) {
		super(
				"REPORT_NOT_FOUND",
				"신고를 찾을 수 없습니다.",
				Map.of("reportId", reportId)
		);
	}
}
