package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.gemini.AdminService;
import com.jslhrd.yorimichi.gemini.dto.request.RegionStoreRequest;
import com.jslhrd.yorimichi.gemini.dto.response.StoreNameRegionResponse;
import com.jslhrd.yorimichi.service.ApiKeyService;
import com.jslhrd.yorimichi.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService adminService;
	private final RegionService regionService;
	private final ApiKeyService apiKeyService;

	@GetMapping("/ai/region-stores")
	public String showRegionStorePage(Model model) {
		model.addAttribute("sidos", regionService.findAllSido());
		model.addAttribute("sigungus", regionService.findAllSigungu());
		model.addAttribute("emds", regionService.findAllEmd());
		model.addAttribute("mapsEmbedKey", apiKeyService.findApiKey("maps"));
		return "/admin/ai/region-stores";
	}

	@ResponseBody
	@PostMapping("/ai/region-stores")
	public List<StoreNameRegionResponse> regionStore(@RequestBody RegionStoreRequest request) {
		return adminService.findStores(request);
	}

}