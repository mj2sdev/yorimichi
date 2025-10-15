package com.jslhrd.yorimichi.config;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.google.common.reflect.TypeToken;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.Schema;
import com.google.gson.Gson;
import com.jslhrd.yorimichi.domain.StoreDTO;

import lombok.RequiredArgsConstructor;

@Component
@DependsOn("geminiConfig")
@RequiredArgsConstructor
public class GeminiHelper {

	private final Client client;

	private final Gson gson;

	public final Integer MAX_LENGTH = 20;

	@Value("${gemini.mime-type}")
	private String mimeType;

	@Value("${gemini.model}")
	private String model;

	private Schema simpleListStringSchema() {
		Schema stringSchema = Schema.builder()
			.type(String.class.getSimpleName())
			.build();
	
		return Schema.builder()
			.type(Array.class.getSimpleName())
			.items(stringSchema)
			.build();
	}

	private Schema storeDtoSchema() {
		Schema stringSchema = Schema.builder()
			.type(String.class.getSimpleName())
			.build();

		return Schema.builder()
			.type(Object.class.getSimpleName())
			.properties(
				Map.of(
					"name", stringSchema,
					"description", stringSchema,
					"phone", stringSchema
				)
			).build();
	}

	private GenerateContentConfig storeDtoConfig() {
		return GenerateContentConfig.builder()
			.responseMimeType(mimeType)
			.responseSchema(storeDtoSchema())
			.build();
	}

	private GenerateContentConfig simpleListStringConfig() {
		return GenerateContentConfig.builder()
			.responseMimeType(mimeType)
			.responseSchema(simpleListStringSchema())
			.build();
	}

	private Type simpleListStringType() {
		return new TypeToken<List<String>>() {}.getType();
	}

	private Type storeDtoType() {
		return new TypeToken<StoreDTO>() {}.getType();
	}

	public StoreDTO storePrompt(String prompt) {
		String jsonData = client.models.generateContent(
			model,
			prompt,
			storeDtoConfig()
		).text();

		return gson.fromJson(jsonData, storeDtoType());
	}

	public List<String> listStringPrompt(String prompt) {
		String jsonData = client.models.generateContent(
			model,
			prompt,
			simpleListStringConfig()
		).text();

		return gson.fromJson(jsonData, simpleListStringType());
	}

	public String simplePrompt(String prompt) {
		return client.models.generateContent(
			model,
			prompt, 
			null
		).text();
	}
}
