package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.RoadDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RoadMapper {
	int insert(RoadDTO dto); // 도로명 등록
	RoadDTO selectById(@Param("id") Long id); // 단건 조회
	List<RoadDTO> selectBySigunguId(@Param("sigunguId") Long sigunguId); // 시군구 기준 목록
	int update(RoadDTO dto); // 이름 수정
	int deleteById(@Param("id") Long id); // 삭제(PK)
	int deleteBySigunguId(@Param("sigunguId") Long sigunguId); // 시군구 기준 일괄 삭제
}
