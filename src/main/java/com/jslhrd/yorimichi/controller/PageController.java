// PageController.java
package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.domain.CategoryDTO;
import com.jslhrd.yorimichi.service.CategoryService;
import com.jslhrd.yorimichi.service.manager.StoreManager;
import com.jslhrd.yorimichi.domain.AddressDTO;
import com.jslhrd.yorimichi.domain.StoreDTO;
import com.jslhrd.yorimichi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PageController {

	private final StoreManager storeManager;
	private final CategoryService categoryService;

	@GetMapping({"/", "/index"})
	public String showIndex(Model model) {

		model.addAttribute("recommendedStoreList", storeManager.findAllByRecommend(9));

		// ✅ 카테고리 목록 실데이터 (상위 8개만 노출 예시)
		List<CategoryDTO> categories;
		try {
			categories = categoryService.findAll();
		} catch (Exception e) {
			// 임시 더미: 화면은 계속 동작
			categories = java.util.List.of();
		}
		model.addAttribute("popularCategories",
				categories.isEmpty()
						? java.util.List.of(
						new CategoryDTO() {{
							setId(101L);
							setName("일식");
						}},
						new CategoryDTO() {{
							setId(102L);
							setName("중식");
						}},
						new CategoryDTO() {{
							setId(103L);
							setName("한식");
						}},
						new CategoryDTO() {{
							setId(104L);
							setName("분식");
						}}
				)
						: categories.stream().limit(8).toList()
		);

		return "index";
	}
}
