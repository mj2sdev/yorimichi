package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.gemini.request.FindRegionStoreNamesRequest;
import com.jslhrd.yorimichi.gemini.request.StoreNameRegionResponse;
import com.jslhrd.yorimichi.service.GeminiService;
import com.jslhrd.yorimichi.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

	private final GeminiService geminiService;
	private final RegionService regionService;

	@GetMapping("/ai/region-stores")
	public String showAiRegionStorePage(Model model) {
		model.addAttribute("sidos", regionService.findAllSido());
		model.addAttribute("sigungus", regionService.findAllSigungu());
		model.addAttribute("emds", regionService.findAllEmd());
		return "/admin/ai/region-stores";
	}

	@ResponseBody
	@PostMapping(
			value = "/ai/region-stores",
			consumes = APPLICATION_JSON_VALUE,
			produces = APPLICATION_JSON_VALUE
	)
	public List<StoreNameRegionResponse> findRegionStoreNames(@RequestBody FindRegionStoreNamesRequest req) {
		return geminiService.findStoreNamesByRegion(req.sidoId(), req.sigunguId(), req.emdId(), req.normalizedCount());
	}

}