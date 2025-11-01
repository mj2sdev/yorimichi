package com.jslhrd.yorimichi.gemini.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Tool {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("google_search")
	public Map<String, Object> googleSearch;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("google_maps")
	public Map<String, Object> googleMaps;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonProperty("google_search_retrieval")
	public Map<String, Object> googleSearchRetrieval;

	public static Tool googleSearch() {
		Tool tool = new Tool();
		tool.googleSearch = Map.of();
		return tool;
	}

	public static Tool googleMaps() {
		Tool tool = new Tool();
		tool.googleMaps = Map.of();
		return tool;
	}

	public static Tool googleSearchRetrieval() {
		Tool tool = new Tool();
		tool.googleSearchRetrieval = Map.of();
		return tool;
	}

}
