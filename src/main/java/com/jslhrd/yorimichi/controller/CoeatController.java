package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.domain.CoeatDTO;
import com.jslhrd.yorimichi.domain.CoeatRequestDTO;
import com.jslhrd.yorimichi.service.CoeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CoeatController {

	private final CoeatService coeatService;

	//같이먹기 리스트
	@GetMapping("/coeats")
	public List<CoeatDTO> listCoeat() {
		return coeatService.findAll();
	}

	@GetMapping("/store/{storeId}/coeats")
	public List<CoeatDTO> listCoeatByStore(@PathVariable Long storeId) {
		return coeatService.findAllByStoreId(storeId);
	}


	//같이먹기 상세
	@GetMapping("/coeat/{coeatId}")
	public CoeatDTO getCoeat(@PathVariable("coeatId") Long coeatId) {
		return coeatService.findById(coeatId);
	}

	//같이먹기 작성
	@PostMapping("/store/{storeId}/coeat")
	public void createCoeat(
			@AuthenticationPrincipal(expression = "userId") Long userId,
			@PathVariable("storeId") Long storeId,
			@RequestBody CoeatDTO coeat) {
		coeatService.save(userId, storeId, coeat);
	}

	//같이먹기 수정
	@PatchMapping("/coeat/{coeatId}")
	public void updateCoeat(
			@AuthenticationPrincipal(expression = "userId") Long userId,
			@PathVariable("coeatId") Long coeatId,
			@RequestBody CoeatDTO coeat) {
		coeatService.update(userId, coeatId, coeat);
	}

	//같이먹기 삭제
	@DeleteMapping("/coeat/{coeatId}")
	public void deleteCoeat(
			@AuthenticationPrincipal(expression = "userId") Long userId,
			@PathVariable("coeatId") Long coeatId) {
		coeatService.delete(userId, coeatId);
	}


	//같이먹기 신청
	@PostMapping("/coeat/{coeatId}/requests")
	public void createCoeatRequest(
			@AuthenticationPrincipal(expression = "userId") Long userId,
			@PathVariable("coeatId") Long coeatId,
			@RequestBody CoeatRequestDTO coeatRequest) {
		coeatService.saveCoeatRequest(userId, coeatId, coeatRequest);
	}

	//같이먹기 처리
	@PatchMapping("/coeat/{coeatId}/requests/")
	public void updateCoeatRequestStatus(
			@AuthenticationPrincipal(expression = "userId") Long userId,
			@PathVariable("coeatId") Long coeatId,
			@RequestBody CoeatRequestDTO coeatRequest) {
		coeatService.updateCoeatRequestStatus(userId, coeatId, coeatRequest);
	}

	//같이먹기 취소
	@DeleteMapping("/coeat/{coeatId}/requests")
	public void deleteCoeatRequest(
			@AuthenticationPrincipal(expression = "userId") Long userId,
			@PathVariable("coeatId") Long coeatId
	) {
		coeatService.cancelCoeatRequest(userId, coeatId);
	}
}