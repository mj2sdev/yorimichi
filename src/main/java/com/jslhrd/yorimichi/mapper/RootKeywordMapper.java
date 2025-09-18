package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.RootKeywordDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RootKeywordMapper {
	int insert(RootKeywordDTO dto); // 루트-키워드 매핑 등록
	RootKeywordDTO selectById(@Param("id") Long id); // 단건 조회
	List<RootKeywordDTO> selectByRootId(@Param("rootId") Long rootId); // 루트 기준 목록
	List<RootKeywordDTO> selectByKeywordId(@Param("keywordId") Long keywordId); // 키워드 기준 목록
	int deleteById(@Param("id") Long id); // 삭제(PK)
	int deleteByRootId(@Param("rootId") Long rootId); // 루트 기준 일괄 삭제
	int deleteByKeywordId(@Param("keywordId") Long keywordId); // 키워드 기준 일괄 삭제
}
