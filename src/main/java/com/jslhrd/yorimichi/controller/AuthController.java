package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.service.AccountService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
@RequiredArgsConstructor
public class AuthController {
	
	private final AccountService accountService;

	//로그아웃
	@PostMapping("/logout")
	public String logout(Principal principal) {
		return "redirect:/index";
	}

	//회원가입 페이지로 이동  PageController에서 이동시켰습니다. 잘못 실행되는 걸 방지하기 위함.
	@GetMapping("/signup")
	public String showSignup(){
		return "user/signup";
	}

	//회원가입 작업 실행
	@PostMapping("/signup")
	public String signup(@RequestBody UserDTO user) {
		accountService.signup(user);
		return "redirect:user/login";
	}

	//소셜회원가입 작업 실행
	@PostMapping("/signup/social")
	public String signupSocial(@RequestBody String token) {
		accountService.signupSocial(token);
		return "redirect:index";
	}

	//닉네임 중복검사
	@ResponseBody
	@GetMapping("/signup/nickname")
	public boolean validateNickname(@RequestParam String nickname) {
		return accountService.validateNickname(nickname);
	}

	//이메일 인증
	@ResponseBody
	@PostMapping("/signup/email/verification")
	public boolean verificateEmail(@RequestBody String email) {
		return accountService.verificateEmail(email);
	}
	
	//로그인 화면으로 이동 PageController에서 옮겼습니다. 헷갈리지 않기 위해.
	@GetMapping("/login")
	public String showLogin(){
		return "user/login";
	}
	
	@PostMapping("/login")
	public String login(@RequestBody UserDTO user) {

		return "redirect:index";
	}

	@PostMapping("/login/social")
	public String socialLogin(){
		
		return "redirect:index";
	}
	
	


	
	
}
