package com.jslhrd.yorimichi.controller;

import java.util.List;

public record PlaceIdsRequest(
		List<String> placeIds
) {
}
