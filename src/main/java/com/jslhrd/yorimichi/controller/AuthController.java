package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.service.AccountService;
import com.jslhrd.yorimichi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;



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
	public String showSignup(@ModelAttribute UserDTO user){
		return "user/signup";
	}

	@PostMapping("/signup")
	public String signup(UserDTO user) {
		accountService.signup(user);
		return "redirect:/";
	}

	@ResponseBody
	@GetMapping("/signup/nickname")
	public boolean validateNickname(@RequestParam("nickname") String nickname) {
		return accountService.validateNickname(nickname);
	}

	@ResponseBody
	@PostMapping("/signup/email/verification")
	public boolean verificateEmail(@RequestBody UserDTO user) {
		final String email = user.getEmail();
		return accountService.verificateEmail(email);
	}
}
