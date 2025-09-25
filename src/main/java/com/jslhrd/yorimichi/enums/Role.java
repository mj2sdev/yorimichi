package com.jslhrd.yorimichi.enums;

public enum Role {

	USER("USER"),
	ADMIN("ADMIN");

	Role(String name) {
		this.name = name;
	}

	private String name;
}
