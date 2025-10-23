package com.jslhrd.yorimichi.api;

import com.jslhrd.yorimichi.domain.response.SliceResponse;
import com.jslhrd.yorimichi.domain.response.StationDTO;
import com.jslhrd.yorimichi.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class StationApiController {

	private final StationService stationService;

	@GetMapping("/mini-stations")
	public SliceResponse<StationDTO> moreList(
			@RequestParam(value = "rootId", required = false) Long rootId,
			@RequestParam(value = "size", defaultValue = "5") int size) {
		return stationService.findSlice(rootId, size);
	}
}