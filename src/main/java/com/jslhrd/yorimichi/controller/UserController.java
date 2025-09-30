package com.jslhrd.yorimichi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jslhrd.yorimichi.domain.UserDTO;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;





@Controller
@RequestMapping("/user")
public class UserController {
	
	//유저 상세페이지 이동
	@GetMapping("/detail/{id}")
	public String showUserDetail(@PathVariable Long userId) {
		return "user/detail";
	}

	//마이페이지 이동
	@GetMapping("/mypage")
	public String showMypage() {
		return "user/mypage";
	}

	//자기 정보 수정
	@ResponseBody
	@PutMapping("mypage")
	public UserDTO updateMyDetail(@RequestBody UserDTO user) {
		//서비스에서 다 처리하고 user를 다시 가져와서 페이지만 비동기로 갱신해주기
		return user;
	}

	//개인정보 보호 설정
	@ResponseBody
	@PutMapping("/mypage/privacy")
	public void putMethodName() {
		//얘 어떻게 처리하면 좋을지 모르겠습니다. 일단 버튼 딸깍식으로 있긴 한데.
	}


	
	
	
}
