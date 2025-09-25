package com.jslhrd.yorimichi.enums;

public enum CoeatRequestStatus {

	PENDING("PENDING"),
	APPROVED("APPROVED"),
	REJECTED("REJECTED"),
	CANCELLED("CANCELLED");

	private String name;

	CoeatRequestStatus(String name) {
		this.name = name;
	}
}
