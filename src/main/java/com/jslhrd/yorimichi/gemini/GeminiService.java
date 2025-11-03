package com.jslhrd.yorimichi.gemini;

import com.jslhrd.yorimichi.enums.ToolMode;
import com.jslhrd.yorimichi.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiService {

	private final ApiKeyService apiKeyService;
	private final RestClient geminiRestClient;

	@Value("${gemini.model:gemini-2.5-flash}")
	private String model;

	private static String requireNonEmpty(String apiKey) {
		if (apiKey == null || apiKey.isBlank()) throw new IllegalStateException("Gemini API key not found");
		return apiKey;
	}

	public String generateWithSearch(String prompt) {
		return generate(prompt, ToolMode.SEARCH, null, null);
	}

	public String generateWithMaps(String prompt) {
		return generate(prompt, ToolMode.MAPS, null, null);
	}

	public String generateWithBoth(String prompt) {
		return generate(prompt, ToolMode.BOTH, null, null);
	}

	public String generatePlain(String prompt) {
		String apiKey = resolveApiKey();

		Map<String, Object> body = Map.of(
				"contents", List.of(Map.of(
						"role", "user",
						"parts", List.of(Map.of("text", prompt))
				))
		);

		return geminiRestClient.post()
				.uri("/models/{model}:generateContent", model)
				.header("x-goog-api-key", apiKey)
				.body(body)
				.retrieve()
				.onStatus(HttpStatusCode::isError, (req, res) -> {
					byte[] bytes = res.getBody() != null ? res.getBody().readAllBytes() : new byte[0];
					String msg = new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
					throw new RuntimeException("Gemini error " + res.getStatusCode().value() + ": " + msg);
				})
				.body(String.class);
	}

	private String resolveApiKey() {
		String apiKey = null;
		try {
			apiKey = apiKeyService.findApiKey("gemini");
		} catch (Throwable ignore) {
		}
		if (apiKey == null || apiKey.isBlank()) throw new IllegalStateException("Gemini API key not configured");
		return apiKey;
	}


	public String generate(String prompt, ToolMode toolMode, Double latitude, Double longitude) {

		String apiKey = resolveApiKey();

		Map<String, Object> body = new LinkedHashMap<>();

		body.put("contents", List.of(Map.of(
				"role", "user",
				"parts", List.of(Map.of("text", prompt))
		)));

		body.put("tools", buildTools(toolMode));

		// 툴 사용 시 MIME 강제 금지 (2.5에서 400 방지)
		if (latitude != null && longitude != null) {
			body.put("toolConfig", Map.of(
					"retrievalConfig", Map.of(
							"latLng", Map.of(
									"latitude", latitude,
									"longitude", longitude
							)
					)
			));
		}

		return geminiRestClient.post()
				.uri("/models/{model}:generateContent", model)
				.header("x-goog-api-key", apiKey)
				.body(body)
				.retrieve()
				.onStatus(HttpStatusCode::isError, (req, res) -> {
					byte[] bytes = res.getBody() != null ? res.getBody().readAllBytes() : new byte[0];
					String msg = new String(bytes, StandardCharsets.UTF_8);
					throw new RuntimeException("Gemini error " + res.getStatusCode().value() + ": " + msg);
				})
				.body(String.class);
	}

	private List<Map<String, Object>> buildTools(ToolMode toolMode) {
		List<Map<String, Object>> tools = new ArrayList<>();
		switch (toolMode) {
			case SEARCH -> tools.add(Map.of("google_search", Map.of()));
			case MAPS -> tools.add(Map.of("googleMaps", Map.of()));
			case BOTH -> {
				tools.add(Map.of("google_search", Map.of()));
				tools.add(Map.of("googleMaps", Map.of()));
			}
		}
		return tools;
	}
}