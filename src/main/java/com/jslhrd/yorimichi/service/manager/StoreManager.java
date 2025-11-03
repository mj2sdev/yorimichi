package com.jslhrd.yorimichi.service.manager;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jslhrd.yorimichi.domain.CategoryDTO;
import com.jslhrd.yorimichi.domain.SearchDTO;
import com.jslhrd.yorimichi.domain.StoreDTO;
import com.jslhrd.yorimichi.mapper.SearchMapper; // 단일/카테고리 조회를 SearchMapper 재사용
import com.jslhrd.yorimichi.service.StoreService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreManager implements StoreService {

    private final SearchMapper mapper;

    @Override
    public StoreDTO findById(Long id) {
        if (id == null) return null;
        return mapper.selectStoreById(id);
    }

    @Override
    public List<CategoryDTO> findCategoriesByStoreId(Long storeId) {
        if (storeId == null) return List.of();
        return mapper.selectCategoriesByStoreId(storeId);
    }
	    // ---------- 나머지는 임시 빈 구현 ----------
    @Override
    public List<StoreDTO> findAll(SearchDTO store) {
        return List.of();
    }

    @Override
    public List<StoreDTO> findAllByUserLike(Long userId) {
        return List.of();
    }

    @Override
    public List<StoreDTO> findAllByRecommend(int limit) {
        return List.of();
    }

    @Override
    public void save(StoreDTO store) {}

    @Override
    public void update(Long storeId, StoreDTO store) {}

    @Override
    public void delete(Long storeId) {}
}
