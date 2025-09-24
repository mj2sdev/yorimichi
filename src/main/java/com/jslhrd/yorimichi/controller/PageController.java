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
		return "/index";
	}

	//정류장으로 이동
	@GetMapping("/station")
	public String showStation() {
		//사용자의 id를 바탕으로 팔로우, 팔로워 등을 가져올 수 있어야 함
		//같이먹기나와 리뷰의 리스트를 가져올 수 있어야함
		return "/station/list";
	}

	//로그인 화면으로 이동
	@GetMapping("/login")
	public String showLogin(){
		return "/user/login";
	}
	


}
