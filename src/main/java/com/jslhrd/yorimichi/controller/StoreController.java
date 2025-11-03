package com.jslhrd.yorimichi.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import com.jslhrd.yorimichi.domain.CoeatDTO;
import com.jslhrd.yorimichi.service.ApiKeyService;
import com.jslhrd.yorimichi.service.BookmarkService;
import com.jslhrd.yorimichi.service.LikeService;
import com.jslhrd.yorimichi.service.StoreService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

	private final StoreService storeService;

	private final LikeService likeService;

	private final BookmarkService bookmarkService;

	private final ApiKeyService apiKeyService;

	//인덱스 페이지에서 가게 맛집 리스트를 보여주기 위함. 인기있는 최신 6개를 보여주면 될 거 같음.
	//인기의 기준이 뭔지 모르겠음.
	@GetMapping("/search")
	public String showStores() {
		return "store/list";
	}


	//첫검색 혹은 검색 버튼을 눌러서 검색.
	// /search/async?keyword={keyword}&tags={tags}
	@ResponseBody
	@GetMapping("/search/sync")
	public String showStoresBySearch(
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "tags", required = false) String tags
	) {
		//List<StoreDTO> stores
		//List<StoreCategoryDTO> categories
		//List<StoreFacilityCategoryDTO> Facilities
		return "store/list";
	}

	//비동기로 리스트를 가져오기 위한 메서드
	//카테고리나 시설 등 버튼을 눌러 설정이 바뀌면 리스트가 달라지는 형태.
	// /search/async?keyword={keyword}&tags={tags}
	@ResponseBody
	@GetMapping("/search/async")
	public void showStoresByFilter(
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "tags", required = false) String tags
	) {
		//서비스로 가서 키워드로 가져온 가게 리스를 보여줄 수 있어야 함.
		//return List<StoreDTO> stores;
	}

	//가게 상세정보로 이동
	@GetMapping("/detail/{storeId:\\d+}")
	public String showDetail(
		@PathVariable("storeId") Long storeId,
		@AuthenticationPrincipal(expression = "userId") Long userId,
		Model model) {
		model.addAttribute("coeatDto", new CoeatDTO());
		model.addAttribute("store", storeService.findById(storeId, userId));
		model.addAttribute("mapKey", apiKeyService.findApiKey("maps", null));
		return "store/detail";
	}


	//북마크 등록
	@ResponseBody
	@PostMapping("/bookmark/{storeId:\\d+}")
	public void submitBookmark(
		@AuthenticationPrincipal(expression = "userId") Long userId,
		@PathVariable("storeId") Long storeId) {
		bookmarkService.save(userId, storeId);
	}

	//북마크 삭제
	@ResponseBody
	@DeleteMapping("/bookmark/{storeId:\\d+}")
	public void deleteBookmark(
		@AuthenticationPrincipal(expression = "userId") Long userId,
		@PathVariable("storeId") Long storeId){
		bookmarkService.delete(userId, storeId);
	}

	@ResponseBody
	@PostMapping("/like/{storeId:\\d+}")
	public void submitLike(
		@AuthenticationPrincipal(expression = "userId") Long userId,
		@PathVariable("storeId") Long storeId) {
		likeService.save(userId, storeId);
	}
	@ResponseBody
	@DeleteMapping("/like/{storeId:\\d+}")
	public void deleteLikes(
		@AuthenticationPrincipal(expression = "userId") Long userId,
		@PathVariable("storeId") Long storeId) {
		likeService.delete(userId, storeId);
	}
}
