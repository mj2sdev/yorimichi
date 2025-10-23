package com.jslhrd.yorimichi.api;

import com.jslhrd.yorimichi.domain.CategoryDTO;
import com.jslhrd.yorimichi.domain.SliceResponse;
import com.jslhrd.yorimichi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CategoryApiController {

	private final CategoryService categoryService;

	@GetMapping("/categories")
	public SliceResponse<CategoryDTO> moreList(
			@RequestParam(value = "categoryId", required = false) Long categoryId,
			@RequestParam(value = "size", defaultValue = "5") int size) {
		return categoryService.findSlice(categoryId, size);
	}
}