package com.jslhrd.yorimichi.service.manager;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jslhrd.yorimichi.domain.CategoryDTO;
import com.jslhrd.yorimichi.domain.SearchResultDTO;
import com.jslhrd.yorimichi.domain.StoreDTO;
import com.jslhrd.yorimichi.domain.SuggestDTO;
import com.jslhrd.yorimichi.mapper.SearchMapper;
import com.jslhrd.yorimichi.service.SearchService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SearchManager implements SearchService {

    private final SearchMapper mapper;

    // ===== 자동완성 =====
    @Override
    public List<SuggestDTO> suggest(String q, int limit) {
        String q2 = (q == null) ? "" : q.trim();
        if (q2.isEmpty()) return List.of();

        int safeLimit = Math.max(1, Math.min(50, limit));
        int half = Math.max(1, safeLimit / 2);

        List<SuggestDTO> stores  = mapper.selectStoreSuggest(q2, half);
        List<SuggestDTO> regions = mapper.selectRegionSuggest(q2, safeLimit - half);

        return Stream.concat(stores.stream(), regions.stream())
                     .limit(safeLimit)
                     .toList();
    }

    // ===== 검색 페이지 =====
    @Override
    public SearchResultDTO search(String q, String type, Long id, int page, int size) {
        String q2    = (q == null) ? "" : q.trim();
        String type2 = (type == null) ? "" : type.trim().toLowerCase();
        int page1    = Math.max(1, page);
        int size1    = Math.max(1, Math.min(50, size));
        int offset   = (page1 - 1) * size1;

        SearchResultDTO result = new SearchResultDTO();
        result.setQ(q2);
        result.setType(type2);
        result.setId(id);
        result.setPage(page1);
        result.setSize(size1);

        // 1) 자동완성 클릭: type=store & id != null → 단건 + 카테고리
        if ("store".equals(type2) && id != null) {
            StoreDTO store = mapper.selectStoreById(id);
            List<CategoryDTO> categories = mapper.selectCategoriesByStoreId(id);

            result.setStore(store);
            result.setCategories(categories);
            result.setTotal((store != null) ? 1L : 0L);
            result.setItems((store != null) ? List.of(store) : List.of());
            return result;
        }

        // 2) 일반 키워드 검색: 상점명 LIKE
        long total = mapper.countStoresByKeyword(q2);
        List<StoreDTO> items = mapper.findStoresByKeyword(q2, offset, size1);

        result.setTotal(total);
        result.setItems(items);
        return result;
    }
}
