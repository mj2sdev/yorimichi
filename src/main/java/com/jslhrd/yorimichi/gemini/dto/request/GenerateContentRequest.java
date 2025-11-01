package com.jslhrd.yorimichi.gemini.dto.request;

import java.util.List;

public record GenerateContentRequest(
		List<Content> contents,
		List<Tool> tools,
		GenerationConfig generationConfig) {


}