package com.jslhrd.yorimichi.config;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class GeminiConfig {

	@Bean
	RestClient geminiRestClient(
			@Value("${gemini.base-url}") String baseUrl,
			@Value("${gemini.mime-type}") String mimeType
	) {
		PoolingHttpClientConnectionManager cm = PoolingHttpClientConnectionManagerBuilder.create()
				.setMaxConnTotal(200)
				.setMaxConnPerRoute(50)
				.build();

		RequestConfig reqCfg = RequestConfig.custom()
				.setConnectTimeout(Timeout.ofSeconds(10))
				.setResponseTimeout(Timeout.ofSeconds(90))
				.build();

		HttpClient http = HttpClients.custom()
				.setConnectionManager(cm)
				.setDefaultRequestConfig(reqCfg)
				.build();

		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(http);

		return RestClient.builder()
				.requestFactory(factory)
				.baseUrl(baseUrl)
				.defaultHeader("Content-Type", mimeType)
				.build();
	}
}