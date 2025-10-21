package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.domain.BookmarkDTO;
import com.jslhrd.yorimichi.domain.ReviewDTO;
import com.jslhrd.yorimichi.domain.StoreDTO;
import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.service.BookmarkService;
import com.jslhrd.yorimichi.service.RelationshipService;
import com.jslhrd.yorimichi.service.ReviewService;
import com.jslhrd.yorimichi.service.StoreService;
import com.jslhrd.yorimichi.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

	private final UserService userService;
	private final StoreService storeService;
	private final ReviewService reviewService;
	private final RelationshipService relationshipService;
	private final BookmarkService bookmarkService;
	//유저 상세페이지 이동
	@GetMapping("/detail/{id}")
	public String showUserDetail(@PathVariable("id") Long userId, Model model) {
		UserDTO user = userService.findById(userId);
		model.addAttribute("user", user);
		return "user/detail";
	}

	//마이페이지 이동
	@GetMapping("/mypage")
	public String showMypage(@AuthenticationPrincipal(expression = "userId") Long userId, Model model) {
		UserDTO user = userService.findById(userId);
		model.addAttribute("user", user);
		return "user/mypage";
	}

	//자기 정보 수정
	@ResponseBody
	@PutMapping("/mypage")
	public void updateMyDetail(@AuthenticationPrincipal(expression = "userId") Long userId, @ModelAttribute UserDTO user) {
		userService.update(userId, user);
	}
}