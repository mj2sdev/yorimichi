package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.RegionSigunguDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RegionSigunguMapper {
	int insert(RegionSigunguDTO dto); // 시군구 등록
	RegionSigunguDTO selectById(@Param("id") Long id); // 단건 조회
	List<RegionSigunguDTO> selectBySidoId(@Param("sidoId") Long sidoId); // 시도 기준 목록
	int update(RegionSigunguDTO dto); // 이름 수정
	int deleteById(@Param("id") Long id); // 삭제(PK)
	int deleteBySidoId(@Param("sidoId") Long sidoId); // 시도 기준 일괄 삭제
}
