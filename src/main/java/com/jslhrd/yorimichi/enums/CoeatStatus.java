package com.jslhrd.yorimichi.enums;

public enum CoeatStatus {

	OPEN("OPEN"),
	CLOSED("CLOSED"),
	CANCELLED("CANCELLED");

	private String name;

	CoeatStatus(String name) {
		this.name = name;
	}
}
