package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.StoreFacilityCategoryDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface StoreFacilityCategoryMapper {
    int insert(StoreFacilityCategoryDTO storeFacilityCategory); // 매핑 추가
    int delete(StoreFacilityCategoryDTO storeFacilityCategory); // 매핑 삭제
    int count(StoreFacilityCategoryDTO storeFacilityCategory); // 매핑 존재 여부
    List<StoreFacilityCategoryDTO> listByStoreId(Long storeId); // 특정 상점의 시설 카테고리 목록
    List<StoreFacilityCategoryDTO> listByFacilityCategoryId(Long facilityCategoryId); // 특정 시설 카테고리에 속한 상점 매핑 목록
}
