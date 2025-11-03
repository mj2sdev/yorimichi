package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.service.ApiKeyService;
import com.jslhrd.yorimichi.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

	private final RegionService regionService;
	private final ApiKeyService apiKeyService;

	@GetMapping("/index")
	public String showIndexPage() {
		return "admin/index";
	}

	@GetMapping("/places/search")
	public String showRegionStorePage(Model model) {
		model.addAttribute("sidos", regionService.findAllSido());
		model.addAttribute("sigungus", regionService.findAllSigungu());
		model.addAttribute("emds", regionService.findAllEmd());
		model.addAttribute("mapsEmbedKey", apiKeyService.findApiKey("maps"));
		return "admin/gemini/search";
	}

	@GetMapping("/reviews/summary")
	public String showReviewSummaryPage() {
		return "admin/reviews/summary";
	}

	@GetMapping("/reviews/keywords")
	public String showReviewKeywordsPage() {
		return "admin/reviews/keywords";
	}
}