package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.domain.response.SliceResponse;
import com.jslhrd.yorimichi.domain.response.StationDTO;
import com.jslhrd.yorimichi.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 정류장(피드) 컨트롤러
 * <p>
 * - /station : 정류장 페이지 이동 (뷰 렌더)
 * - /feed, /feed/{count} : 인덱스 우측 미니 정류장 위젯용 API (더미 데이터 반환)
 * <p>
 * 실제 서비스 연동 전, 화면/디자인 검증 목적으로 더미 데이터를 내려줍니다.
 * 나중에 서비스/매퍼 연동 시 makeDummy() 대신 서비스 호출로 교체하면 됩니다.
 */
@Controller
@RequiredArgsConstructor
public class StationController {

	private final StationService stationService;

	/**
	 * 정류장 페이지
	 */
	@GetMapping("/station")
	public String showStation(Model model) {

		SliceResponse<StationDTO> stationSlice = stationService.findSlice(null, 5);
		model.addAttribute("stationSlice", stationSlice);

		return "station/list";
	}

}