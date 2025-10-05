package com.jslhrd.yorimichi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateReportException extends RuntimeException {

	public DuplicateReportException(Long userId, Long rootId) {
		super("Report exists user id " + userId + " and root id " + rootId);
	}
}
