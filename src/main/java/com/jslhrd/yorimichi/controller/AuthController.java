package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.domain.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
public class AuthController {

	//로그아웃
	@PostMapping("/logout")
	public String logout() {
		return "redirect:/index";
	}

	//회원가입 페이지로 이동  PageController에서 이동시켰습니다. 잘못 실행되는 걸 방지하기 위함.
	@GetMapping("/signup")
	public String showSignup() {
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

	@PostMapping("/login")
	public String login(@RequestBody UserDTO user) {

		return "redirect:/index";
	}

	@PostMapping("/login/social")
	public String socialLogin() {

		return "redirect:/index";
	}


}
