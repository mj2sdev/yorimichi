package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.domain.BookmarkDTO;
import com.jslhrd.yorimichi.domain.ReviewDTO;
import com.jslhrd.yorimichi.domain.StoreDTO;
import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.service.RelationshipService;
import com.jslhrd.yorimichi.service.ReviewService;
import com.jslhrd.yorimichi.service.StoreService;
import com.jslhrd.yorimichi.service.UserService;
import lombok.RequiredArgsConstructor;
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

	//유저 상세페이지 이동
	@GetMapping("/detail/{id}")
	public String showUserDetail(@PathVariable("id") Long userId, Model model) {
		UserDTO user = userService.findById(userId);
		List<ReviewDTO> reviews = reviewService.findAllByUserId(null);
		List<StoreDTO> likes = storeService.findAllByUserLike(null);
		List<BookmarkDTO> bookmarks = null;
		List<UserDTO> follow = relationshipService.findFollowees(null);
		List<UserDTO> follower = relationshipService.findFollowers(null);
		List<UserDTO> block = relationshipService.findBlocks(null);
		model.addAttribute("user", user);
		model.addAttribute("reviews", reviews);
		model.addAttribute("likes", likes);
		model.addAttribute("bookmarks", bookmarks);
		model.addAttribute("follow", follow);
		model.addAttribute("follower", follower);
		model.addAttribute("block", block);
		return "user/detail";
	}

	//마이페이지 이동
	@GetMapping("/mypage")
	public String showMypage(Principal principal, Model model) {
		UserDTO user = userService.findById(null);
		List<ReviewDTO> reviews = reviewService.findAllByUserId(null);
		List<StoreDTO> likes = storeService.findAllByUserLike(null);
		List<BookmarkDTO> bookmarks = null;
		List<UserDTO> follow = relationshipService.findFollowees(null);
		List<UserDTO> follower = relationshipService.findFollowees(null);
		List<UserDTO> block = relationshipService.findBlocks(null);
		model.addAttribute("user", user);
		model.addAttribute("reviews", reviews);
		model.addAttribute("likes", likes);
		model.addAttribute("bookmarks", bookmarks);
		model.addAttribute("follow", follow);
		model.addAttribute("follower", follower);
		model.addAttribute("block", block);
		return "user/mypage";
	}

	//자기 정보 수정
	@ResponseBody
	@PutMapping("/mypage")
	public void updateMyDetail(Principal principal, @ModelAttribute UserDTO user) {
		userService.update(user);
	}
}
