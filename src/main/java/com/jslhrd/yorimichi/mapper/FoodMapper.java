package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.FoodDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FoodMapper {
	int insert(FoodDTO row); // 음식 등록
	FoodDTO selectById(@Param("id") Long id); // 단건 조회
	List<FoodDTO> selectByStoreId(@Param("storeId") Long storeId); // 매장 기준 목록
	List<FoodDTO> selectPaged(@Param("offset") int offset, @Param("limit") int limit); // 페이지 목록
	int update(FoodDTO row); // 정보 수정
	int deleteById(@Param("id") Long id); // 삭제(PK)
	int deleteByStoreId(@Param("storeId") Long storeId); // 매장 삭제 시 일괄 삭제
}
