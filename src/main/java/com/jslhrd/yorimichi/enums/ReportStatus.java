package com.jslhrd.yorimichi.enums;

public enum ReportStatus {

	PENDING("PENDING"),
	IN_REVIEW("IN_REVIEW"),
	RESOLVED("RESOLVED"),
	REJECTED("REJECTED"),
	CANCELLED("CANCELLED");

	private String name;

	private ReportStatus(String name) {
		this.name = name;
	}
}
