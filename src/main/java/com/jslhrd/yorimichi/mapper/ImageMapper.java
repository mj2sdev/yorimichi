package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.ImageDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ImageMapper {
	int insert(ImageDTO row); // 이미지 등록
	ImageDTO selectById(@Param("id") Long id); // 단건 조회
	List<ImageDTO> selectByRootId(@Param("rootId") Long rootId); // 루트 기준 목록
	int deleteById(@Param("id") Long id); // 삭제(PK)
	int deleteByRootId(@Param("rootId") Long rootId); // 루트 기준 일괄 삭제
}
