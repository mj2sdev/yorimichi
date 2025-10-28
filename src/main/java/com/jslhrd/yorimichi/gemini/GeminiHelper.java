package com.jslhrd.yorimichi.gemini;

import com.google.common.reflect.TypeToken;
import com.google.genai.Client;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.Schema;
import com.google.gson.Gson;
import com.jslhrd.yorimichi.gemini.request.StoreIngestRequest;
import com.jslhrd.yorimichi.gemini.request.StoreNameRegionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.List;

import static com.google.genai.types.Type.Known;

@Component
@DependsOn("geminiConfig")
@RequiredArgsConstructor
public class GeminiHelper {

	public final Integer MAX_LENGTH = 20;

	private final Client client;
	private final Gson gson;

	@Value("${gemini.mime-type}")
	private String mimeType;

	@Value("${gemini.model}")
	private String model;

	public List<StoreNameRegionResponse> listNameRegionPairs(String prompt, int count) {
		String json = client.models.generateContent(
				model,
				prompt,
				nameRegionListConfig(count)
		).text();
		return gson.fromJson(json, nameRegionListType());
	}

	private GenerateContentConfig nameRegionListConfig(int count) {
		return GenerateContentConfig.builder()
				.responseMimeType(mimeType)
				.responseSchema(Schemas.nameRegionListSchema(count))
				.build();
	}

	private Type nameRegionListType() {
		return new TypeToken<List<StoreNameRegionResponse>>() {
		}.getType();
	}

	public StoreIngestRequest generateStore(String prompt) {
		String jsonData = client.models.generateContent(
				model,
				prompt,
				storeConfig()
		).text();

		return gson.fromJson(jsonData, storeType());
	}

	private GenerateContentConfig storeConfig() {
		return GenerateContentConfig.builder()
				.responseMimeType(mimeType)
				.responseSchema(Schemas.storeSchema())
				.build();
	}

	private Type storeType() {
		return new TypeToken<StoreIngestRequest>() {
		}.getType();
	}


	private GenerateContentConfig simpleListStringConfig() {
		return GenerateContentConfig.builder()
				.responseMimeType(mimeType)
				.responseSchema(simpleListStringSchema())
				.build();
	}

	private Schema simpleListStringSchema() {
		return Schema.builder()
				.type(Known.ARRAY)
				.items(Schemas.STRING)
				.build();
	}

	public List<String> listStringPrompt(String prompt) {
		String jsonData = client.models.generateContent(
				model,
				prompt,
				simpleListStringConfig()
		).text();

		return gson.fromJson(jsonData, simpleListStringType());
	}

	private Type simpleListStringType() {
		return new TypeToken<List<String>>() {
		}.getType();
	}

	public String simplePrompt(String prompt) {
		return client.models.generateContent(
				model,
				prompt,
				null
		).text();
	}
}