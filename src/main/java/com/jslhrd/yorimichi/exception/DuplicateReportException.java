package com.jslhrd.yorimichi.exception;

import java.util.Map;

public class DuplicateReportException extends DomainException {

	public DuplicateReportException(Long userId, Long rootId) {
		super(
				"DUPLICATE_REPORT_REQUEST",
				"이미 신고되어 있습니다.",
				Map.of(
						"userId", userId,
						"rootId", rootId
				)
		);
	}
}
