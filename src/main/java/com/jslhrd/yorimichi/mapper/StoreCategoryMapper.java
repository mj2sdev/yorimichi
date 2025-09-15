package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.StoreCategoryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface StoreCategoryMapper {
	int insert(StoreCategoryDTO row); // 매장-카테고리 매핑 등록
	StoreCategoryDTO selectById(@Param("id") Long id); // 단건 조회
	List<StoreCategoryDTO> selectByStoreId(@Param("storeId") Long storeId); // 매장 기준 목록
	List<StoreCategoryDTO> selectByCategoryId(@Param("categoryId") Long categoryId); // 카테고리 기준 목록
	int deleteById(@Param("id") Long id); // 삭제(PK)
	int deleteByStoreId(@Param("storeId") Long storeId); // 매장 기준 일괄 삭제
	int deleteByCategoryId(@Param("categoryId") Long categoryId); // 카테고리 기준 일괄 삭제
}
