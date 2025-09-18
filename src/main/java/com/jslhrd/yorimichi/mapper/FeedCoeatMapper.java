package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.FeedCoeatDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FeedCoeatMapper {
	int insert(FeedCoeatDTO dto); // 코잇 생성
	List<FeedCoeatDTO> selectByFeedId(@Param("feedId") Long feedId); // 피드 기준 목록
	List<FeedCoeatDTO> selectByHostUserId(@Param("userId") Long userId); // 호스트 기준 목록
	List<FeedCoeatDTO> selectPaged(@Param("offset") int offset, @Param("limit") int limit); // 페이지 목록
	int update(FeedCoeatDTO dto); // 내용/상태 수정
	int deleteByFeedId(@Param("feedId") Long feedId); // 피드 기준 일괄 삭제
	/* 2차 수정: dto로 이름 통일, id를 받는것들 삭제 */
}
