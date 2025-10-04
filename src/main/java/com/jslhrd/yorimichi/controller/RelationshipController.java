package com.jslhrd.yorimichi.controller;

import org.springframework.web.bind.annotation.RestController;

import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.service.RelationshipService;

import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
public class RelationshipController {

	private final RelationshipService relationshipService;

	//사용자가 팔로우 한 리스트
	@GetMapping("/follow")
	public List<UserDTO> showFollow(Principal principal) {
		//TODO: userid
		long userId = 0;
		return relationshipService.findFollowById(userId);

	}

	//사용자가 팔로우 당한 리스트
	@GetMapping("/follower")
	public List<UserDTO> showFollower(Principal principal) {
		//TODO: userid
		long userId = 0;
		return relationshipService.findFollowerById(userId);
	}

	//사용자가 상대 팔로우
	@PostMapping("/follow/{targetId}")
	public void followUser(Principal principal, @PathVariable("targetId") Long targetId) {
		//TODO: userid
		long userId = 0;
		relationshipService.saveFollow(userId, targetId);
	}

	//사용자가 상대와의 관계를 삭제
	@DeleteMapping("/follow/{targetId}")
	public void unfollowUser(Principal principal, @PathVariable("targetId") Long targetId) {
		//TODO: userid
		long userId = 0;
		relationshipService.delete(userId, targetId);
	}

	//사용자가 차단한 대상 목록
	@GetMapping("/block")
	public List<UserDTO> shoeBlockUser(Principal principal) {
		long userId = 0;
		return relationshipService.findBlockById(userId);
	}
	
	
	//사용자가 상대를 차단
	@PostMapping("/block/{targetId}")
	public void blockuser(Principal principal, @PathVariable("targetId") Long targetId) {
		//TODO: userid
		long userId = 0;;
		relationshipService.saveBlock(userId, targetId);
	}

	//사용자가 상대를 차단해제
	@DeleteMapping("/block/{targetId}")
	public void unblockuser(Principal principal, @PathVariable("targetId") Long targetId) {
		//TODO: userid
		long userId = 0;;
		relationshipService.deleteBlock(userId, targetId);
	}
}
