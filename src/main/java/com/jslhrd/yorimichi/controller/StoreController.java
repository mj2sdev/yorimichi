package com.jslhrd.yorimichi.controller;

import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.jslhrd.yorimichi.domain.StoreDTO;
import com.jslhrd.yorimichi.service.BookmarkService;
import com.jslhrd.yorimichi.service.LikeService;
import com.jslhrd.yorimichi.service.StoreService;
import org.springframework.ui.Model;
import com.jslhrd.yorimichi.mapper.SearchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import com.jslhrd.yorimichi.service.StoreService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

	private final StoreService storeService;
	private final LikeService likeService;
	private final BookmarkService bookmarkService;
	private final SearchMapper mapper; // ✅ 주입
	//인덱스 페이지에서 가게 맛집 리스트를 보여주기 위함. 인기있는 최신 6개를 보여주면 될 거 같음.
	//인기의 기준이 뭔지 모르겠음.
	@ResponseBody
	@GetMapping("/search")
	public void showStores() {

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




	//북마크 등록
	@ResponseBody
	@PostMapping("/bookmark/{storeId}")
	public void submitBookmark(@AuthenticationPrincipal(expression = "userId") Long userId, @PathVariable("storeId") Long storeId) {
		bookmarkService.save(userId, storeId);		
	}

	//북마크 삭제
	@ResponseBody
	@DeleteMapping("/bookmark/{storeId}")
	public void deleteBookmark(@AuthenticationPrincipal(expression = "userId") Long userId, @PathVariable("storeId") Long storeId){
		bookmarkService.delete(userId, storeId);
	}

	@GetMapping("/like")
	public String showLikes() {
		return "store/list";
	}

	@ResponseBody
	@PostMapping("/like/{storeId}")
	public void submitLike(@AuthenticationPrincipal(expression = "userId") Long userId, @PathVariable("storeId") Long storeId) {
		likeService.save(userId, storeId);
	}
	@ResponseBody
	@DeleteMapping("/like/{storeId}")
	public void deleteLikes(@AuthenticationPrincipal(expression = "userId") Long userId, @PathVariable("storeId") Long storeId){
		likeService.delete(userId, storeId);
	}
	    // 가게 상세정보 (PathVariable 버전)
    @GetMapping("/detail/{storeId}")
    public String showDetail(@PathVariable Long storeId, Model model) {
        var store = mapper.selectStoreById(storeId);
        var categories = mapper.selectCategoriesByStoreId(storeId);

        model.addAttribute("store", store);
        model.addAttribute("categories", categories);
        return "store/detail"; // templates/store/detail.html
    }

    // (선택) 쿼리스트링 버전도 허용하고 싶으면 함께 추가
    @GetMapping("/detail")
    public String showDetailByParam(@RequestParam("id") Long id, Model model) {
        return showDetail(id, model); // 위 메서드 재사용
    }
}
