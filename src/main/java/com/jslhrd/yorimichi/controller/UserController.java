package com.jslhrd.yorimichi.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;





@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

	private final UserService userService;
	
	//유저 상세페이지 이동
	@GetMapping("/detail")
	public String showUserDetail(@RequestParam("id") Long userId, Model model) {
		UserDTO user = userService.findById(userId);
		model.addAttribute("user", user);
		return "user/detail";
	}

	//마이페이지 이동
	@GetMapping("/mypage")
	public String showMypage(Principal principal, Model model) {
		UserDTO user = userService.findById(null);
		model.addAttribute("user", user);
		return "user/mypage";
	}

	//자기 정보 수정
	@ResponseBody
	@PutMapping("mypage")
	public void updateMyDetail(Principal principal) {
		//userService.update(user);
	}

	//개인정보 보호 설정
	@ResponseBody
	@PutMapping("/mypage/privacy")
	public void putMethodName(@RequestBody UserDTO dto) {
		//TODO:얘 어떻게 처리하면 좋을지 모르겠습니다. 일단 버튼 딸깍식으로 있긴 한데. 도움 요청합니다.
		/*
		바로 위에 putMapping("mypage") << 여기랑 비슷하게 처리하시면 됩니다. 
		dto 에는 내용이 추가되어야 합니다.
		dto
		- hiddenReview (Boolean)
		- hiddenLike (Boolean)
		- hiddenFavorite (Boolean)
		- hiddenFollow (Boolean)
		- hiddenFollower (Boolean)
		userService.updatePrivacy(dto)
		*/
	}


	
	
	
}
