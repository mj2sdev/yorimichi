package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.FeedDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FeedMapper {
	int insert(FeedDTO dto); // 피드 작성
	FeedDTO selectById(@Param("id") Long id); // 단건 조회
	List<FeedDTO> selectByUserId(@Param("userId") Long userId); // 작성자 기준 목록
	List<FeedDTO> selectPaged(@Param("offset") int offset, @Param("limit") int limit); // 페이지 목록
	int update(FeedDTO dto); // 내용/공개여부 등 수정
	int deleteById(@Param("id") Long id); // 삭제(PK)
}
