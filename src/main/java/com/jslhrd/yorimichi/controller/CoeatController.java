package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.domain.CoeatDTO;
import com.jslhrd.yorimichi.domain.CoeatRequestDTO;
import com.jslhrd.yorimichi.service.CoeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CoeatController {

	private final CoeatService coeatService;

	//같이먹기 리스트
	@GetMapping("/coeats")
	public List<CoeatDTO> showCoeatList() {
		return coeatService.findAll();
	}

	//특정 가게 같이먹기 리스트
	@GetMapping("/coeats/{id}")
	public List<CoeatDTO> getMethodName(@PathVariable("id") Long storeId) {
		return coeatService.findAllByStoreId(storeId);
	}


	//같이먹기 상세
	@GetMapping("/coeat/{id}")
	public CoeatDTO showCoeat(@PathVariable("id") Long coeatId) {
		return coeatService.findById(coeatId);
	}

	//같이먹기 작성
	@PostMapping("/coeat")
	public void postCoeat(@RequestBody CoeatDTO coeat, Principal principal) {
		//TODO: 유저id 찾아오는 커스텀 USER
		long userId = 0;
		long storeId = 0;
		coeatService.save(userId, storeId, coeat);
	}

	//같이먹기 수정
	@PutMapping("/coeat/{id}")
	public void putCoeat(@PathVariable("id") Long coeatId, @RequestBody CoeatDTO coeat, Principal principal) {
		//TODO: 유저id 찾아오는 커스텀 USER
		long userId = 0;
		coeatService.update(userId, coeatId, coeat);
	}

	//같이먹기 삭제
	@DeleteMapping("/coeat/{id}")
	public void deleteCoeat(@PathVariable("id") Long coeatId, Principal principal) {
		//TODO: 유저id 찾아오는 커스텀 USER
		long userId = 0;
		coeatService.delete(userId, coeatId);
	}

	//같이먹기 신청
	@PostMapping("/coeat/{coeatId}/participant")
	public void participateCoeat(
			@PathVariable("coeatId") Long coeatId,
			@RequestBody CoeatRequestDTO coeatRequest,
			Principal principal) {
		long userId = 0;
		coeatService.saveCoeatRequest(userId, coeatId, coeatRequest);
	}

	//같이먹기 수락/거절
	//대기 상태에서 수락/거절 상태로 변경하는 거로 처리될 것 같아요
	@PatchMapping("/coeat/{coeatId}/participant/{participantId}")
	public void acceptParticipant(
			@PathVariable("coeatId") Long coeatId,
			@PathVariable("participantId") Long participantId,
			Principal principal) {
		long userId = 0;
		CoeatRequestDTO coeatRequest = new CoeatRequestDTO();
		coeatService.updateCoeatRequestStatus(userId, coeatId, coeatRequest);
	}

	//같이먹기 제외/거절/취소
	@DeleteMapping("/coeat/{coeatid}/participant")
	public void deleteParticipant(
			@PathVariable("coeatId") Long coeatId,
			@PathVariable("participantId") Long participantId,
			Principal principal) {
		long userId = 0;
		coeatService.cancelCoeatRequest(userId, coeatId);
	}
}
