package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.*;
import com.jslhrd.yorimichi.exception.StoreNotFoundException;
import com.jslhrd.yorimichi.mapper.*;
import com.jslhrd.yorimichi.service.StoreService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class StoreManager implements StoreService {

	private final RootMapper rootMapper;
	private final StoreMapper storeMapper;
	private final CategoryMapper categoryMapper;
	private final FacilityCategoryMapper facilityCategoryMapper;
	private final ReviewMapper reviewMapper;
	private final LikeMapper likeMapper;
	private final ImageMapper imageMapper;
	private final AddressMapper addressMapper;

	@Override
	public PageResult<StoreDTO> findAll(SearchDTO q) {

		int total = storeMapper.countAll(q);

		q.setTotalCount(total);
		q.computePagination();

		if (total == 0) return new PageResult<StoreDTO>(List.of(), q.getPage(), q.getRecordCount(), total);

		// 1) 라이트 목록 (addressText, repImageUrl 포함)
		List<StoreDTO> stores = storeMapper.selectAll(q);

		// 2) 키 수집
		List<Long> storeIds = stores.stream().map(StoreDTO::getId).toList();
		List<Long> addressIds = stores.stream().map(StoreDTO::getAddressId).distinct().toList();

		// 3) 배치 조회
		List<Map<String, Object>> catRows = categoryMapper.selectByStoreIds(storeIds);          // storeId, id, name
		List<Map<String, Object>> facRows = facilityCategoryMapper.selectByStoreIds(storeIds);  // storeId, id, name
		List<Map<String, Object>> aggRows = reviewMapper.selectAvgAndCountByStoreIds(storeIds); // storeId, avgRating, reviewCount
		List<Map<String, Object>> likeRows = likeMapper.selectCountByRootIds(storeIds);         // rootId, likeCount
		List<Map<String, Object>> addrRows = addressMapper.selectTextByIds(addressIds);         // addressId, roadAddressText
		List<Map<String, Object>> imgRows = imageMapper.selectByRootIds(storeIds);              // rootId, url

		// 4) 그룹핑 맵
		// 카테고리: rows = [{storeId, id, name}, ...]
		Map<Long, List<CategoryDTO>> catsByStore = new HashMap<>();
		for (var m : catRows) {
			long sid = ((Number) m.get("storeId")).longValue();
			CategoryDTO dto = new CategoryDTO();
			dto.setId(((Number) m.get("id")).longValue());
			dto.setName((String) m.get("name"));
			catsByStore.computeIfAbsent(sid, k -> new ArrayList<>()).add(dto);
		}

		// 시설 카테고리: stores = [{storeId, id, name}, ...]
		Map<Long, List<FacilityCategoryDTO>> facsByStore = new HashMap<>();
		for (var m : facRows) {
			long sid = ((Number) m.get("storeId")).longValue();
			FacilityCategoryDTO dto = new FacilityCategoryDTO();
			dto.setId(((Number) m.get("id")).longValue());
			dto.setName((String) m.get("name"));
			facsByStore.computeIfAbsent(sid, k -> new ArrayList<>()).add(dto);
		}

		Map<Long, Double> avg = new HashMap<>();
		Map<Long, Integer> revCnt = new HashMap<>();
		for (var m : aggRows) {
			Long sid = ((Number) m.get("storeId")).longValue();
			Object a = m.get("avgRating");
			avg.put(sid, a == null ? null : ((Number) a).doubleValue());
			revCnt.put(sid, ((Number) m.get("reviewCount")).intValue());
		}

		Map<Long, Integer> likeCnt = new HashMap<>();
		for (var m : likeRows) {
			likeCnt.put(((Number) m.get("rootId")).longValue(), ((Number) m.get("likeCount")).intValue());
		}

		Map<Long, String> addrText = new HashMap<>();
		for (var m : addrRows) {
			addrText.put(((Number) m.get("addressId")).longValue(), (String) m.get("addressText"));
		}

		Map<Long, String> repImg = new HashMap<>();
		for (var m : imgRows) {
			repImg.put(((Number) m.get("rootId")).longValue(), (String) m.get("url"));
		}

		// 5) DTO 주입
		for (StoreDTO store : stores) {
			store.setCategories(catsByStore.getOrDefault(store.getId(), List.of()));
			store.setFacilities(facsByStore.getOrDefault(store.getId(), List.of()));
			store.setAvgRating(avg.get(store.getId()));
			store.setReviewCount(revCnt.getOrDefault(store.getId(), 0));
			store.setLikeCount(likeCnt.getOrDefault(store.getId(), 0));
			store.setAddressText(addrText.get(store.getAddressId()));
			store.setImageUrl(repImg.get(store.getId()));
		}

		return new PageResult<StoreDTO>(stores, q.getPage(), q.getRecordCount(), total);

	}

	@Override
	public List<StoreDTO> findAllByUserLike(Long userId) {

		// TODO: mappers 구현 후 교체
		throw new UnsupportedOperationException();

	}

	@Override
	public List<StoreDTO> findAllByRecommend(Long userId) {

		// TODO: mappers 구현 후 교체
		throw new UnsupportedOperationException();

	}

	@Override
	public StoreDTO findById(Long storeId) {
		// TODO: review 목록 페이징
		// TODO: coeat 목록 페이징
		return storeMapper.selectById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(storeId));
	}

	@Override
	@Transactional
	public void save(StoreDTO dto) {

		rootMapper.insert(dto);
		int result = storeMapper.insert(dto);

		if (result == 0) {
			log.warn("Store insert affectedResult={} dto={}", result, dto);
			throw new IllegalStateException("Store insert failed. result=" + result + ", dto=" + dto);
		}

		log.info("Store created id={}", dto.getId());
	}

	@Override
	@Transactional
	public void update(StoreDTO dto) {

		int result = storeMapper.update(dto);

		if (result == 0) {
			throw new StoreNotFoundException(dto.getId());
		}

		log.info("Store updated id={}", dto.getId());
	}

	@Override
	@Transactional
	public void delete(Long storeId) {

		int result = storeMapper.deleteById(storeId);

		if (result == 0) {
			throw new StoreNotFoundException(storeId);
		}

		log.info("Store deleted id={}", storeId);
	}
}