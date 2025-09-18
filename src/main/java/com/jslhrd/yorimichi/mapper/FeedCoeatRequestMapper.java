package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.FeedCoeatRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FeedCoeatRequestMapper {
	int insert(FeedCoeatRequestDTO dto); // 참가요청 생성
	List<FeedCoeatRequestDTO> selectByFeedId(@Param("feedId") Long feedId); // 코잇 기준 목록
	List<FeedCoeatRequestDTO> selectByUserId(@Param("userId") Long userId); // 요청자 기준 목록
	int existsByFeedIdAndUserId(@Param("feedId") Long feedId, @Param("userId") Long userId); // 중복 여부(XML: COUNT(*))
	int updateStatus(FeedCoeatRequestDTO dto); // 승인/거절 등 상태 변경
	int deleteByFeedId(@Param("feedId") Long feedId); // 코잇 삭제 시 일괄 삭제
}
