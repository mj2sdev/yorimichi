package com.jslhrd.yorimichi.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.jslhrd.yorimichi.domain.CategoryDTO;
import com.jslhrd.yorimichi.domain.StoreDTO;
import com.jslhrd.yorimichi.domain.SuggestDTO;

@Mapper
public interface SearchMapper {

    // ------ 자동완성 ------
    List<SuggestDTO> selectStoreSuggest(@Param("q") String q, @Param("limit") int limit);
    List<SuggestDTO> selectRegionSuggest(@Param("q") String q, @Param("limit") int limit);

    // ------ 검색 페이지 ------
    long countStoresByKeyword(@Param("q") String q);

    List<StoreDTO> findStoresByKeyword(@Param("q") String q,
                                       @Param("offset") int offset,
                                       @Param("size") int size);

    StoreDTO selectStoreById(@Param("id") Long id);

    List<CategoryDTO> selectCategoriesByStoreId(@Param("storeId") Long storeId);
}
