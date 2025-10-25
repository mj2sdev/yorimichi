// PageController.java
package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.domain.AddressDTO;
import com.jslhrd.yorimichi.domain.StoreDTO;
import com.jslhrd.yorimichi.service.CategoryService;
import com.jslhrd.yorimichi.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PageController {

	private final CategoryService categoryService;   // ✅ 주입
	private final StationService stationService;

	@GetMapping({"/", "/index"})
	public String showIndex(Model model) {

		// (기존: 중앙/하단 카드용 더미 9개)
		List<StoreDTO> list = new ArrayList<>();
		for (int i = 1; i <= 9; i++) {
			StoreDTO s = new StoreDTO();
			s.setName("요리미치 이자카야 " + i);
			s.setDescription("따뜻한 분위기 + 정갈한 안주 " + i + "호점");
			AddressDTO addr = new AddressDTO();
			addr.setRoadAddressText("대전 서구 둔산로 " + (120 + i));
			s.setAddress(addr);
			list.add(s);
		}
		model.addAttribute("recommendedStoreList", list);

		// ✅ 카테고리 목록 실데이터 (상위 5개만 노출 예시)
		model.addAttribute("categorySlice", categoryService.findSlice(null, 10));

		// ✅ 미니 정류장 목록 (상위 5개만 노출 예시)
		model.addAttribute("stationSlice", stationService.findSlice(null, 10));

		// ✅ 중앙/3x3 더미 이미지 (picsum)
		var imageUrls = List.of(
				"https://picsum.photos/seed/yorimichi1/600/400",
				"https://picsum.photos/seed/yorimichi2/600/400",
				"https://picsum.photos/seed/yorimichi3/600/400"
		);
		model.addAttribute("imageUrls", imageUrls);
		model.addAttribute("heroImageUrl", imageUrls.get((int) (System.currentTimeMillis() / 1000) % imageUrls.size()));

		return "index";
	}

}
