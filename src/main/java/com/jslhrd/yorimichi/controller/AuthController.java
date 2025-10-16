package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.service.AccountService;
import com.jslhrd.yorimichi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class AuthController {

	private final AccountService accountService;
	private final UserService userService;

	@GetMapping("/login")
	public String showLogin() {
		return "user/login";
	}

	@GetMapping("/signup")
	public String showSignup(@ModelAttribute UserDTO user) {
		return "user/signup";
	}

	@PostMapping("/signup")
	public String signup(@ModelAttribute UserDTO user) {
		accountService.signupLocal(user);
		return "redirect:/login";
	}

	@ResponseBody
	@GetMapping("/signup/nickname")
	public boolean validateNickname(@RequestParam("nickname") String nickname) {
		return userService.isNicknameAvailable(nickname);
	}

	@ResponseBody
	@PostMapping("/signup/email/verification")
	public boolean verificateEmail(@RequestParam("token") String token) {
		return accountService.confirmEmailVerification(token);
	}
}
