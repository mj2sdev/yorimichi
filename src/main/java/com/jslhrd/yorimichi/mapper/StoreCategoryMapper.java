package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.StoreCategoryDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface StoreCategoryMapper {
    int insert(StoreCategoryDTO storeCategory); // 매핑 추가
    int delete(StoreCategoryDTO storeCategory); // 매핑 삭제
    int count(StoreCategoryDTO storeCategory); // 매핑 존재 여부
    List<StoreCategoryDTO> listByStoreId(Long storeId); // 특정 상점의 카테고리 목록
    List<StoreCategoryDTO> listByCategoryId(Long categoryId); // 특정 카테고리에 속한 상점 매핑 목록
}
