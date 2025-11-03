package com.jslhrd.yorimichi.gemini;

import com.jslhrd.yorimichi.domain.*;
import com.jslhrd.yorimichi.gemini.dto.request.StoreRequest;
import com.jslhrd.yorimichi.gemini.dto.response.StoreDetailResponse;
import com.jslhrd.yorimichi.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GeminiStoreManager {

	private final AddressService addressService;
	private final StoreService storeService;
	private final ImageService imageService;
	private final CategoryService categoryService;
	private final FacilityCategoryService facilityCategoryService;
	private final FoodService foodService;

	@Transactional
	public void save(StoreRequest request) {

		StoreDetailResponse detail = request.detail();

		// 1) 주소 upsert
		AddressDTO addressDTO = new AddressDTO();
		addressDTO.setEmdId(request.emdId());
		addressDTO.setDetail(detail.detail());
		addressDTO.setPlaceId(detail.placeId());
		addressDTO.setJibunAddressText(detail.jibunAddressText());
		addressDTO.setRoadAddressText(detail.roadAddressText());

		addressService.upsert(addressDTO);
		Long addressId = addressDTO.getId();

		// 2) 상점 저장
		StoreDTO store = new StoreDTO();
		store.setAddressId(addressId);
		store.setName(detail.name());
		store.setDescription(detail.description());
		store.setPhone(detail.phone());

		storeService.save(store);
		Long storeId = store.getId();

		// 3) 이미지: URL 목록을 root_image로 연결
		if (detail.images() != null && !detail.images().isEmpty()) {
			detail.images().forEach(url -> {
				Long imageId = imageService.getOrCreateByName(new ImageDTO(url));
				imageService.addImageToRoot(storeId, imageId);
			});
		}

		// 4) 카테고리: 존재/미존재에 상관없이 getOrCreate → store_category 링크
		if (detail.categories() != null) {
			detail.categories().forEach(name -> {
				Long categoryId = categoryService.getOrCreateByName(new CategoryDTO(name));
				categoryService.addCategoryToStore(storeId, categoryId);
			});
		}

		// 5) 시설 카테고리
		if (detail.facilities() != null) {
			detail.facilities().forEach(name -> {
				Long facilityId = facilityCategoryService.getOrCreateByName(new FacilityCategoryDTO(name));
				facilityCategoryService.addFacilityCategoryToStore(storeId, facilityId);
			});
		}

		// 6) 메뉴: 서비스 내부에서 FOOD root 생성까지 포함하면 가장 깔끔함 (권장)
		if (detail.menus() != null) {
			for (StoreDetailResponse.Menu food : detail.menus()) {
				if (food == null) continue;
				FoodDTO foodDTO = new FoodDTO();
				foodDTO.setStoreId(storeId);
				foodDTO.setName(food.name());
				foodDTO.setPrice(food.price());
				foodDTO.setDescription(food.description());
				foodService.save(foodDTO);
			}
		}
	}
}