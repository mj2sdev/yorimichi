package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.RootDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RootMapper {
	int insert(RootDTO row); // 루트 등록
	RootDTO selectById(@Param("id") Long id); // 단건 조회
	List<RootDTO> selectPaged(@Param("offset") int offset, @Param("limit") int limit); // 페이지 목록
	int update(RootDTO row); // 내용/상태 수정
	int deleteById(@Param("id") Long id); // 삭제(PK)
}
