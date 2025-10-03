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

	private final StoreService storeService;
	private final CategoryService categoryService;

	//인덱스로 이동
	@GetMapping("/index")
	public String showIndex(Model model) {
		List<StoreDTO> stores = storeService.findAll();
		List<CategoryDTO> categories = categoryService.findAll();
		model.addAttribute("stores", stores);
		model.addAttribute("categories", categories);
		return "index";
	}
}
