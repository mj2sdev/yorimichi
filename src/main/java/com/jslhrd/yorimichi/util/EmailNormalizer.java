package com.jslhrd.yorimichi.util;

import org.springframework.stereotype.Component;

import java.text.Normalizer;

@Component
public class EmailNormalizer {

	public String normalize(String rawEmail) {
		if (rawEmail == null) return null;
		String normalizedEmail = rawEmail.strip(); // 앞뒤 공백 제거
		normalizedEmail = Normalizer.normalize(normalizedEmail, Normalizer.Form.NFKC); // 유니코드 정규화(정책 선택)
		normalizedEmail = normalizedEmail.toLowerCase(); // 소문자화
		return normalizedEmail;
	}
}