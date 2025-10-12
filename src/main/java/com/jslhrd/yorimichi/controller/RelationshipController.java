package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.service.RelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class RelationshipController {

	private final RelationshipService relationshipService;

	//사용자가 팔로우 한 리스트
	@GetMapping("/follow")
	public List<UserDTO> showFollow(Principal principal) {
		//TODO: userid
		long userId = 0;
		return relationshipService.findFollowees(userId);

	}

	//사용자가 팔로우 당한 리스트
	@GetMapping("/follower")
	public List<UserDTO> showFollower(Principal principal) {
		//TODO: userid
		long userId = 0;
		return relationshipService.findFollowers(userId);
	}

	//사용자가 상대 팔로우
	@PostMapping("/follow/{targetId}")
	public void followUser(Principal principal, @PathVariable("targetId") Long targetId) {
		//TODO: userid
		long userId = 0;
		relationshipService.saveFollow(userId, targetId);
	}

	//사용자가 상대 언팔로우
	@DeleteMapping("/follow/{targetId}")
	public void unfollowUser(Principal principal, @PathVariable("targetId") Long targetId) {
		//TODO: userid
		long userId = 0;
		relationshipService.deleteFollow(userId, targetId);
	}

	//사용자가 차단한 대상 목록
	@GetMapping("/block")
	public List<UserDTO> shoeBlockUser(Principal principal) {
		long userId = 0;
		return relationshipService.findBlocks(userId);
	}


	//사용자가 상대를 차단
	@PostMapping("/block/{targetId}")
	public void blockuser(Principal principal, @PathVariable("targetId") Long targetId) {
		//TODO: userid
		long userId = 0;
		;
		relationshipService.saveBlock(userId, targetId);
	}

	//사용자가 상대를 차단해제
	@DeleteMapping("/block/{targetId}")
	public void unblockuser(Principal principal, @PathVariable("targetId") Long targetId) {
		//TODO: userid
		long userId = 0;
		;
		relationshipService.deleteBlock(userId, targetId);
	}
}
