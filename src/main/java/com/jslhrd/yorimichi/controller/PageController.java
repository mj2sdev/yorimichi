package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.advice.GlobalExceptionHandler;
import com.jslhrd.yorimichi.domain.CategoryDTO;
import com.jslhrd.yorimichi.domain.StoreDTO;
import com.jslhrd.yorimichi.service.CategoryService;
import com.jslhrd.yorimichi.service.StoreService;


import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class PageController {
	// private final GlobalExceptionHandler globalExceptionHandler;
	// PageController(GlobalExceptionHandler globalExceptionHandler) {
	// 	this.globalExceptionHandler = globalExceptionHandler;
	// }
	
	private final CategoryService categoryService;
	private final StoreService storeService;

	
	//인덱스로 이동
	@GetMapping("/index")
	public String showIndex(Model model) {
		//맛집 리스트를 가져와야함, 최신기준
		//음식 카테고리를 가져와야함
		List<CategoryDTO> categories = categoryService.findAll(null);
		List<StoreDTO> stores = storeService.findAll(null);
		
		model.addAttribute("categories", categories);
		model.addAttribute("stores", stores);
		
		return "index";
	}
}
