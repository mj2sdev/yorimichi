package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.StoreDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface StoreMapper {
	int insert(StoreDTO dto); // 매장 등록
	StoreDTO selectById(@Param("id") Long id); // 단건 조회
	List<StoreDTO> selectByOwnerId(@Param("ownerId") Long ownerId); // 점주 기준 목록
	List<StoreDTO> selectPaged(@Param("offset") int offset, @Param("limit") int limit); // 페이지 목록
	int update(StoreDTO dto); // 정보 수정
	int deleteById(@Param("id") Long id); // 삭제(PK)
	int deleteByOwnerId(@Param("ownerId") Long ownerId); // 점주 탈퇴 시 일괄 삭제
}
