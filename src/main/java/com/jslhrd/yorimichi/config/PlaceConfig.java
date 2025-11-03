package com.jslhrd.yorimichi.config;

import com.google.common.net.HttpHeaders;
import com.jslhrd.yorimichi.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class PlaceConfig {

	private final ApiKeyService apiKeyService;

	@Bean
	RestClient placesRestClient(@Value("${google.places.base-url}") String baseUrl) {
		return RestClient.builder()
				.baseUrl(baseUrl)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.requestInterceptor((request, body, execution) -> {
					String apiKey = apiKeyService.findApiKey("place");
					request.getHeaders().set("X-Goog-Api-Key", apiKey);
					return execution.execute(request, body);
				})
				.build();
	}
}
