package com.jslhrd.yorimichi.gemini.dto.request;

import java.util.List;

public record Content(
		String role,
		List<Part> parts) {
}
