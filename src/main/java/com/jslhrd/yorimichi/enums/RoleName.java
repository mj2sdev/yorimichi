package com.jslhrd.yorimichi.enums;

import lombok.Getter;

@Getter
public enum RoleName {

	USER,
	ADMIN;

	public static RoleName from(String value) {
		if (value == null) throw new IllegalArgumentException("role is null");
		return RoleName.valueOf(value.toUpperCase());
	}

	public String asAuthority() {
		return "ROLE_" + name(); // "ROLE_USER", "ROLE_ADMIN"
	}
}