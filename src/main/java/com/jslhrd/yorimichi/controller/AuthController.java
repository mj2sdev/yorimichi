package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class AuthController {

	private final AccountService accountService;

	@GetMapping("/login")
	public String showLogin() {
		return "user/login";
	}

	@GetMapping("/signup")
	public String showSignup() {
		return "user/signup";
	}

	@PostMapping("/signup")
	public String signup(@ModelAttribute UserDTO user) {
		accountService.signupLocal(user);
		return "redirect:/login";
	}

	@ResponseBody
	@GetMapping("/signup/nickname")
	public boolean validateNickname(@RequestParam String nickname) {
		return accountService.isNicknameAvailable(nickname);
	}

	@ResponseBody
	@PostMapping("/signup/email/verification")
	public boolean verificateEmail(@RequestParam("token") String token) {
		return accountService.confirmEmailVerification(token);
	}
}
