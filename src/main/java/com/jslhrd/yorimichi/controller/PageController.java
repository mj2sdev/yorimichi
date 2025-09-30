package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.advice.GlobalExceptionHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PageController {

	private final GlobalExceptionHandler globalExceptionHandler;

	PageController(GlobalExceptionHandler globalExceptionHandler) {
		this.globalExceptionHandler = globalExceptionHandler;
	}

	//인덱스로 이동
	@GetMapping("/index")
	public String showIndex() {
		//맛집 리스트를 가져와야함, 최신기준
		//음식 카테고리를 가져와야함
		return "index";
	}
}
