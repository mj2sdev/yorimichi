package com.jslhrd.yorimichi.gemini.store;

import com.jslhrd.yorimichi.domain.*;
import com.jslhrd.yorimichi.gemini.store.dto.request.SaveStoresRequest;
import com.jslhrd.yorimichi.gemini.store.dto.response.SaveStoresResponse;
import com.jslhrd.yorimichi.gemini.store.dto.response.StoreDetailResponse;
import com.jslhrd.yorimichi.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeminiStoreManager implements GeminiStoreService {

	private final AddressService addressService;
	private final StoreService storeService;
	private final ImageService imageService;
	private final CategoryService categoryService;
	private final FacilityCategoryService facilityCategoryService;
	private final FoodService foodService;
	private final RegionService regionService;

	@Override
	public SaveStoresResponse saveAll(SaveStoresRequest request) {

		if (request == null) throw new IllegalArgumentException("request is null");
		if (request.sidoId() == null) throw new IllegalArgumentException("sidoId는 필수입니다.");
		List<StoreDetailResponse> items = Objects.requireNonNull(request.details(), "details must not be null");

		List<SaveStoresResponse.UnresolvedItem> unresolved = new ArrayList<>();
		int savedCount = 0;

		for (int i = 0; i < items.size(); i++) {
			StoreDetailResponse detail = items.get(i);
			try {
				Long finalEmdId = resolveEmdId(
						request.sidoId(),
						request.sigunguId(),
						request.emdId(),
						detail
				).orElseThrow(() -> new IllegalStateException("행정동(emd) 매칭 실패"));

				boolean ok = save(finalEmdId, detail.normalized());
				if (ok) savedCount++;
				else unresolved.add(new SaveStoresResponse.UnresolvedItem(i, detail.name(), "저장 스킵(이미 동일)"));
			} catch (Exception e) {
				unresolved.add(new SaveStoresResponse.UnresolvedItem(i, detail.name(), e.getMessage()));
			}
		}

		return new SaveStoresResponse(savedCount, unresolved);
	}

	@Transactional
	public boolean save(Long emdId, StoreDetailResponse detail) {

		// 1) 주소 upsert
		AddressDTO addressDTO = new AddressDTO();
		addressDTO.setEmdId(emdId);
		addressDTO.setDetail("");
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

		return true;
	}

	private Optional<Long> resolveEmdId(
			Long sidoId,
			Long sigunguId,
			Long emdId,
			StoreDetailResponse detail
	) {

		// 0) sidoId는 호출부에서 필수 보장
		RegionSidoDTO sido = regionService.findBySidoId(sidoId);
		if (sido == null) {
			throw new IllegalArgumentException("유효하지 않은 sidoId: " + sidoId);
		}

		// 1) emdId 직접 제공 → 실존 + 위계 검증
		if (emdId != null) {
			RegionEmdDTO emd = regionService.findByEmdId(emdId);
			if (emd == null) return Optional.empty();

			RegionSigunguDTO sigungu = regionService.findBySigunguId(emd.getSigunguId());
			if (sigungu == null) return Optional.empty();
			if (!Objects.equals(sigungu.getSidoId(), sidoId)) return Optional.empty();

			if (sigunguId != null && !Objects.equals(emd.getSigunguId(), sigunguId)) {
				return Optional.empty();
			}

			return Optional.of(emdId);
		}

		// 2) emdId 미제공 → 이름 기반 탐색
		final String emdName = detail.emdName();
		final String sigunguName = detail.sigunguName();

		// 2-1) sigunguId가 이미 주어지면 그 범위에서 emdName 매칭
		if (sigunguId != null) {
			if (!emdName.isEmpty()) {
				RegionEmdDTO emd = regionService.findByEmdName(sigunguId, emdName);
				if (emd != null) return Optional.of(emd.getId());
			}

			// emdName이 없으면 특정 불가
			return Optional.empty();
		}

		// 2-2) sigunguId가 없고 sigunguName이 있으면: sidoId 범위에서 sigunguName → sigunguId 추론 후 emdName 매칭
		if (!sigunguName.isEmpty() && !emdName.isEmpty()) {
			RegionSigunguDTO sigungu = regionService.findBySigunguName(sidoId, sigunguName);
			if (sigungu != null) {
				Long gid = sigungu.getId();
				RegionEmdDTO emdOpt = regionService.findByEmdName(gid, emdName);
				if (emdOpt != null) return Optional.of(emdOpt.getId());
			}
			return Optional.empty();
		}

		// 그 외(정보 부족)
		return Optional.empty();
	}
}