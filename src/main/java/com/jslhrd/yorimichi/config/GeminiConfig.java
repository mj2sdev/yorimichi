package com.jslhrd.yorimichi.config;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

//@Configuration
//@ConfigurationProperties(prefix = "gemini")
@Getter
@Setter
@Slf4j
public class GeminiConfig {

	private String apiKey;
	private String model;
	private String jsonMimeType;

	@Bean
	public Client geminiClient() {
		try {
			return Client.builder()
				.apiKey(apiKey)
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
