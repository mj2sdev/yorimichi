package com.jslhrd.yorimichi.gemini;

import com.google.genai.types.Schema;
import com.google.genai.types.Type;

import java.util.List;
import java.util.Map;

public final class Schemas {

	// 공통 베이스
	public static final Schema STRING = Schema.builder().type(Type.Known.STRING).build();
	public static final Schema INTEGER = Schema.builder().type(Type.Known.INTEGER).build();
	public static final Schema NUMBER = Schema.builder().type(Type.Known.NUMBER).build();

	private Schemas() {
	}

	// 자주 쓰는 파생(헬퍼)
	public static Schema stringMax(int max) {
		return Schema.builder().type(Type.Known.STRING).maxLength((long) max).build();
	}

	public static Schema arrayOf(Schema items) {
		return Schema.builder().type(Type.Known.ARRAY).items(items).build();
	}

	public static Schema objectOf(Map<String, Schema> props, List<String> required) {
		Schema.Builder b = Schema.builder().type(Type.Known.OBJECT).properties(props);
		if (required != null && !required.isEmpty()) b.required(required);
		return b.build();
	}

	/**
	 * StoreIngestRequest와 1:1 매칭되는 구조화 출력 스키마
	 */
	public static Schema storeSchema() {

		Schema address = objectOf(Map.of(
				"roadAddressText", STRING,
				"jibunAddressText", STRING,
				"detail", STRING,
				"latitude", NUMBER,
				"longitude", NUMBER
		), List.of("roadAddressText", "detail", "latitude", "longitude"));

		Schema food = objectOf(Map.of(
				"name", STRING,
				"price", INTEGER,
				"description", STRING
		), List.of("name", "price"));

		return objectOf(Map.of(
				"name", stringMax(100),
				"description", stringMax(1000),
				"phone", stringMax(20),
				"address", address,
				"categories", arrayOf(STRING),
				"facilities", arrayOf(STRING),
				"images", arrayOf(STRING),
				"foods", arrayOf(food),
				"idempotencyKey", STRING
		), List.of("name", "address"));
	}

	public static Schema nameRegionListSchema(int count) {

		Schema item = objectOf(Map.of(
				"name", stringMax(100),
				"region", STRING,
				"emd", STRING
		), List.of("name", "region", "emd"));

		return Schema.builder()
				.type(Type.Known.ARRAY)
				.items(item)
				.maxItems((long) count)
				.build();
	}
}