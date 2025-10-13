package com.jslhrd.yorimichi.enums;

import lombok.Getter;

@Getter
public enum Role {

	USER,
	ADMIN;

	public static Role from(String value) {
		if (value == null) throw new IllegalArgumentException("role is null");
		return Role.valueOf(value.toUpperCase());
	}

	public String asAuthority() {
		return "ROLE_" + name(); // "ROLE_USER", "ROLE_ADMIN"
	}
}