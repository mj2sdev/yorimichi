package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.service.AccountService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
@RequiredArgsConstructor
public class AuthController {
	
	//private final AccountService accountService;

	//로그아웃
	@PostMapping("/logout")
	public String logout() {
		return "redirect:/index";
	}

	//회원가입 페이지로 이동  PageController에서 이동시켰습니다. 잘못 실행되는 걸 방지하기 위함.
	@GetMapping("/signup")
	public String showSignup(){
		return "/user/signup";
	}

	@PostMapping("/signup")
	public String signup(@RequestBody UserDTO user) {
		
		return "redirect:/index";
	}
	

	@ResponseBody
	@GetMapping("/signup/nickname")
	public boolean validateNickname(@RequestParam String nickname) {
		boolean result = false;

		return result;
	}

	@ResponseBody
	@PostMapping("/signup/email-verification")
	public boolean verificateEmail(@RequestBody String email) {
		boolean result = false;

		return result;
	}

	//로그인 화면으로 이동 PageController에서 옮겼습니다. 헷갈리지 않기 위해.
	@GetMapping("/login")
	public String showLogin(){
		return "/user/login";
	}
	
	@PostMapping("/login")
	public String login(@RequestBody UserDTO user) {

		return "redirect:/index";
	}

	@PostMapping("/login/social")
	public String socialLogin(){
		
		return "redirect:/index";
	}
	
	


	
	
}
