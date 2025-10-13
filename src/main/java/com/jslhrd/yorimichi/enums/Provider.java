package com.jslhrd.yorimichi.enums;

import java.util.Locale;

public enum Provider {

	GOOGLE("GOOGLE");

	private final String name; // spring.security.oauth2.client.registration.<id>

	Provider(String name) {
		this.name = name;
	}

	/**
	 * Security 설정의 registrationId와 매핑 (대소문자/별칭 허용)
	 */
	public static Provider fromName(String id) {
		if (id == null) return null;
		String norm = id.trim().toLowerCase(Locale.ROOT);
		for (Provider p : values()) {
			if (p.name.equals(norm) || p.name().equalsIgnoreCase(norm)) return p;
		}
		throw new IllegalArgumentException("Unknown provider: " + id);
	}
}
