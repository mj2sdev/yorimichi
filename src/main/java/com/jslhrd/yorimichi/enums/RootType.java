package com.jslhrd.yorimichi.enums;

public enum RootType {

	USER("USER"),
	STORE("STORE"),
	FOOD("FOOD"),
	COEAT("COEAT"),
	COMMENT("COMMENT"),
	REVIEW("REVIEW");

	private String type;

	private RootType(String type) {
		this.type = type;
	}
}
