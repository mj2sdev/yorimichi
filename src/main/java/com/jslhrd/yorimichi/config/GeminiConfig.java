package com.jslhrd.yorimichi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class GeminiConfig {

	@Bean
	RestClient geminiRestClient(
			@Value("${gemini.base-url}") String baseUrl
	) {
		return RestClient.builder()
				.baseUrl(baseUrl)
				.defaultHeader("Content-Type", APPLICATION_JSON_VALUE)
				.build();
	}
}