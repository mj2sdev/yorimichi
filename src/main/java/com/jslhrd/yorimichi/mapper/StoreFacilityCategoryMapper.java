package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.StoreFacilityCategoryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface StoreFacilityCategoryMapper {
	int insert(StoreFacilityCategoryDTO row); // 매장-시설 카테고리 매핑 등록
	StoreFacilityCategoryDTO selectById(@Param("id") Long id); // 단건 조회
	List<StoreFacilityCategoryDTO> selectByStoreId(@Param("storeId") Long storeId); // 매장 기준 목록
	List<StoreFacilityCategoryDTO> selectByCategoryId(@Param("categoryId") Long categoryId); // 카테고리 기준 목록
	int deleteById(@Param("id") Long id); // 삭제(PK)
	int deleteByStoreId(@Param("storeId") Long storeId); // 매장 기준 일괄 삭제
	int deleteByCategoryId(@Param("categoryId") Long categoryId); // 카테고리 기준 일괄 삭제
}
