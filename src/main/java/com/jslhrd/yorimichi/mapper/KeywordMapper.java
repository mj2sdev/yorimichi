package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.KeywordDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface KeywordMapper {
	int insert(KeywordDTO row); // 키워드 등록
	KeywordDTO selectById(@Param("id") Long id); // 단건 조회
	List<KeywordDTO> selectByNameLike(@Param("name") String name); // 이름 부분검색
	int update(KeywordDTO row); // 이름 등 수정 (XML: update에서 createdAt 제외)
	int deleteById(@Param("id") Long id); // 삭제(PK)
}
