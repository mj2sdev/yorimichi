// PageController.java
package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.domain.CategoryDTO;
import com.jslhrd.yorimichi.domain.StoreDTO;
import com.jslhrd.yorimichi.domain.response.SliceResponse;
import com.jslhrd.yorimichi.domain.response.StationDTO;
import com.jslhrd.yorimichi.service.CategoryService;
import com.jslhrd.yorimichi.service.StationService;
import com.jslhrd.yorimichi.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PageController {

	private final StoreService storeService;
	private final CategoryService categoryService;
	private final StationService stationService;

	@GetMapping({"/", "/index"})
	public String showIndex(Model model) {

		List<StoreDTO> recommendStores = storeService.findAllByRecommend(9);
		model.addAttribute("recommendedStoreList", recommendStores);

		SliceResponse<CategoryDTO> sliceCategory = categoryService.findSlice(null, 10);
		model.addAttribute("categorySlice", sliceCategory);


		SliceResponse<StationDTO> sliceStation = stationService.findSlice(null, 10);
		model.addAttribute("stationSlice", sliceStation);

		return "index";
	}

}
