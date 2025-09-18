package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.RegionEmdDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RegionEmdMapper {
	int insert(RegionEmdDTO dto); // 행정동 등록
	RegionEmdDTO selectById(@Param("id") Long id); // 단건 조회
	List<RegionEmdDTO> selectBySigunguId(@Param("sigunguId") Long sigunguId); // 시군구 기준 목록
	int update(RegionEmdDTO dto); // 이름 등 수정
	int deleteById(@Param("id") Long id); // 삭제(PK)
	int deleteBySigunguId(@Param("sigunguId") Long sigunguId); // 시군구 기준 일괄 삭제
}
