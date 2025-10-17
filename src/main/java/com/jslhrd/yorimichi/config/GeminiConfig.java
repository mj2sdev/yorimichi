package com.jslhrd.yorimichi.config;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.google.genai.Client;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.jslhrd.yorimichi.service.ApiKeyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@DependsOn("dataSource")
@RequiredArgsConstructor
public class GeminiConfig {

	private final ApiKeyService apiKeyService;

	@Bean
	public Client geminiClient() {
		try {
			String geminiKey = apiKeyService.findApiKey("gemini", "mj2sdev");

			return Client.builder()
				.apiKey(geminiKey)
				.build();
		} catch (Exception e) {
			log.error("gemini api key를 확인해야 합니다.");
			return null;
		}
	}

	@Bean
	public Gson gson() {
		return new GsonBuilder()
			.registerTypeAdapter(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
				@Override
				public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
					return new JsonPrimitive(src.toString());
				}
			})
			.registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
				@Override
				public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
					return LocalDateTime.parse(json.getAsString());
				}
			})
			.create();
	}
	
}
