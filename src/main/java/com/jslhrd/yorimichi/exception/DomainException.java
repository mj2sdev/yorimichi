package com.jslhrd.yorimichi.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class DomainException extends RuntimeException {

	private final String code;
	private final Map<String, Object> meta;

	public DomainException(String code, String message) {
		super(message);
		this.code = code;
		this.meta = Map.of();
	}

	public DomainException(String message, String code, Map<String, Object> meta) {
		super(message);
		this.code = code;
		this.meta = meta;
	}
}
