package com.jslhrd.yorimichi.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jslhrd.yorimichi.domain.CategoryDTO;
import com.jslhrd.yorimichi.domain.FacilityCategoryDTO;
import com.jslhrd.yorimichi.domain.StoreDTO;
import com.jslhrd.yorimichi.service.CategoryService;
import com.jslhrd.yorimichi.service.StoreService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;







@Controller
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

	private final StoreService storeService;
	private final CategoryService categoryService;
	//인덱스 페이지에서 가게 맛집 리스트를 보여주기 위함. 인기있는 최신 6개를 보여주면 될 거 같음.
	//인기의 기준이 뭔지 모르겠음.
	@GetMapping("/search")
	public String showStores(
		@RequestParam(value = "keyword", required = false) String keyword, 
		@RequestParam(value = "tags", required = false) String tags,
		Model model
	) {
		List<StoreDTO> stores = storeService.findAll(null);
		List<CategoryDTO> categories = categoryService.findAll(null); 
		//List<FacilityCategoryDTO> facilities = 

		model.addAttribute("stores", stores);
		model.addAttribute("categories", categories);
		//model.addAttribute("facilities", facilities);
		return "store/list";
	}
	

	//첫검색 혹은 검색 버튼을 눌러서 검색.
	// /search/async?keyword={keyword}&tags={tags}
	@GetMapping("/search/sync")
	public String showStoresBySearch(
		@RequestParam(value = "keyword", required = false) String keyword, 
		@RequestParam(value = "tags", required = false) String tags,
		Model model
	) {
		List<StoreDTO> stores = storeService.findAll(null);
		List<CategoryDTO> categories = categoryService.findAll(null); 
		//List<FacilityCategoryDTO> facilities = 

		model.addAttribute("stores", stores);
		model.addAttribute("categories", categories);
		//model.addAttribute("facilities", facilities);
		return "store/list";
	}
	
	//비동기로 리스트를 가져오기 위한 메서드
	//카테고리나 시설 등 버튼을 눌러 설정이 바뀌면 리스트가 달라지는 형태.
	// /search/async?keyword={keyword}&tags={tags}
	@ResponseBody
	@GetMapping("/search/async")
	public void showStoresByFilter(
		@RequestParam(value = "keyword", required = false) String keyword, 
		@RequestParam(value = "tags", required = false) String tags,
		Model model
	) {
		List<StoreDTO> stores = storeService.findAll(null);
		List<CategoryDTO> categories = categoryService.findAll(null); 
		//List<FacilityCategoryDTO> facilities = 

		model.addAttribute("stores", stores);
		model.addAttribute("categories", categories);
		//model.addAttribute("facilities", facilities);
	}
	
	//가게 상세정보로 이동
	@GetMapping("/detail")
	public String showDetail(@RequestParam(value = "id") Long storeId){
		//StoreDTO
		return "store/detail";
	}


	//북마크 등록
	@ResponseBody
	@PostMapping("/bookmark")
	public void submitBookmark(@RequestBody Long id) {
		//등록 처리 후 북마크 갱신
		//return List<BookmarkDTO> Bookmarks;
	}

	//북마크 삭제
	@ResponseBody
	@DeleteMapping("/bookmark")
	public void deleteBookmark(@RequestBody Long Id){
		//삭제 처리 후 북마크 갱신
		//return List<BookmarkDTO> Bookmarks;
	}

	@ResponseBody
	@GetMapping("/like")
	public String showLikes(Principal principal) {
		//return storeService.findAllByUserLike(null);
		return null;
	}

	@ResponseBody
	@PostMapping("/like")
	public void submitLike(@RequestBody Long storeId) {
		//return List<LikeDTO> likes;
	}

	@ResponseBody
	@DeleteMapping("/like")
	public void deleteLikes(@RequestParam(value = "storeId") Long storeId){
		//return List<LikeDTO> likes;
	}
	
}
