package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.gemini.AdminService;
import com.jslhrd.yorimichi.gemini.dto.request.RegionStoreRequest;
import com.jslhrd.yorimichi.gemini.dto.request.StoreDetailRequest;
import com.jslhrd.yorimichi.gemini.dto.response.StoreDetailResponse;
import com.jslhrd.yorimichi.gemini.dto.response.StoreNameRegionResponse;
import com.jslhrd.yorimichi.places.PlacesService;
import com.jslhrd.yorimichi.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// 새 컨트롤러 예시
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminApiController {

	private final RegionService regionService;
	private final PlacesService placesService;
	private final AdminService adminService;

	/*@PostMapping(
			path = "/places/search",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public List<StoreNameRegionResponse> search(@RequestBody RegionStoreRequest req) {
		String sido = regionService.findBySidoId(req.sidoId()).getName();
		String sigungu = req.sigunguId() != null ? regionService.findBySigunguId(req.sigunguId()).getName() : "";
		String emd = req.emdId() != null ? regionService.findByEmdId(req.emdId()).getName() : "";
		return placesService.searchStores("맛집", sido, sigungu, emd, req.count());
	}

	@PostMapping(
			path = "/places/detail",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public StoreDetailResponse detail(@RequestBody StoreDetailRequest req) {
		return placesService.getDetailEnriched(req.placeId());
	}

	@PostMapping(
			path = "/places/details",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public List<StoreDetailResponse> details(@RequestBody PlaceIdsRequest req) {
		return placesService.getDetails(req.placeIds());
	}*/

	@PostMapping(
			path = "/places/search",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public List<StoreNameRegionResponse> search(@RequestBody RegionStoreRequest req) {
		return adminService.findStores(req);
	}

	@PostMapping(
			path = "/places/detail",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<StoreDetailResponse> detail(@RequestBody StoreDetailRequest req) {
		return adminService.storeDetailByRegionNameAndPlaceId(req)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
}
