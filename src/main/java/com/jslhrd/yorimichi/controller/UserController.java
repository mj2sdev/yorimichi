package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.domain.ImageDTO;
import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.service.GoogleDriveService;
import com.jslhrd.yorimichi.service.ImageService;
import com.jslhrd.yorimichi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

	private final UserService userService;
	private final ImageService imageService;
	private final GoogleDriveService googleDriveService;

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
	public boolean updateMyDetail(
		@AuthenticationPrincipal(expression = "userId") Long userId,
		@RequestParam(name = "rawImage", required = false) MultipartFile userImageFile,
		@ModelAttribute UserDTO user
		) {

			String url = googleDriveService.uploadFile(userImageFile);
			ImageDTO image = new ImageDTO();
			image.setUrl(url);
			long imageId = imageService.save(image);
			imageService.addImageToRoot(userId, imageId);
			return userService.update(userId, user);
	}
}