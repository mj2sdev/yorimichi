package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.RootImageDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RootImageMapper {

	/* 루트 이미지 등록 */
	int insert(RootImageDTO dto);

	/* 루트 기준 이미지 목록 */
	List<RootImageDTO> selectByRootId(@Param("rootId") Long rootId);

	/* 단건 삭제(PK) */
	int deleteById(@Param("id") Long id);

	/* 루트 기준 일괄 삭제 */
	int deleteByRootId(@Param("rootId") Long rootId);
}
