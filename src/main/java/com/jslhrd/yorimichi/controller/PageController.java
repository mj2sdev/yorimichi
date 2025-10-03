package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.advice.GlobalExceptionHandler;

import lombok.RequiredArgsConstructor;

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

	//인덱스로 이동
	@GetMapping("/index")
	public String showIndex(Model model) {
		return "index";
	}
}
