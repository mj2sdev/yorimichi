package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.RegionSidoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RegionSidoMapper {
	int insert(RegionSidoDTO row); // 시도 등록
	RegionSidoDTO selectById(@Param("id") Long id); // 단건 조회
	List<RegionSidoDTO> selectAll(); // 전체 목록
	int update(RegionSidoDTO row); // 이름 수정
	int deleteById(@Param("id") Long id); // 삭제(PK)
}
