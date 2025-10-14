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
		List<ReviewDTO> reviews = reviewService.findAllByUserId(userId);
		List<StoreDTO> likes = storeService.findAllByUserLike(userId);
		List<StoreDTO> bookmarks = bookmarkService.findBookmarks(userId);
		List<UserDTO> followees = relationshipService.findFollowees(userId);
		List<UserDTO> followers = relationshipService.findFollowers(userId);
		List<UserDTO> blocks = relationshipService.findBlocks(userId);
		model.addAttribute("user", user);
		model.addAttribute("reviews", reviews);
		model.addAttribute("likes", likes);
		model.addAttribute("bookmarks", bookmarks);
		model.addAttribute("followees", followees);
		model.addAttribute("followers", followers);
		model.addAttribute("blocks", blocks);
		return "user/detail";
	}

	//마이페이지 이동
	@GetMapping("/mypage")
	public String showMypage(Principal principal, Model model) {
		Long userId = (long) 1;
		UserDTO user = userService.findById(userId);
		List<ReviewDTO> reviews = reviewService.findAllByUserId(userId);
		List<StoreDTO> likes = storeService.findAllByUserLike(userId);
		List<StoreDTO> bookmarks = bookmarkService.findBookmarks(userId);
		List<UserDTO> followees = relationshipService.findFollowees(userId);
		List<UserDTO> followers = relationshipService.findFollowers(userId);
		List<UserDTO> blocks = relationshipService.findBlocks(userId);
		model.addAttribute("user", user);
		model.addAttribute("reviews", reviews);
		model.addAttribute("likes", likes);
		model.addAttribute("bookmarks", bookmarks);
		model.addAttribute("followees", followees);
		model.addAttribute("followers", followers);
		model.addAttribute("blocks", blocks);
		return "user/mypage";
	}

	//자기 정보 수정
	@ResponseBody
	@PutMapping("/mypage")
	public void updateMyDetail(Principal principal, @ModelAttribute UserDTO user) {
		userService.update(user);
	}
}
