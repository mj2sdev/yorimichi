package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.FeedCoeatRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface FeedCoeatRequestMapper {
	int insert(FeedCoeatRequestDTO row); // 참가요청 생성
	FeedCoeatRequestDTO selectById(@Param("id") Long id); // 단건 조회
	List<FeedCoeatRequestDTO> selectByCoeatId(@Param("coeatId") Long coeatId); // 코잇 기준 목록
	List<FeedCoeatRequestDTO> selectByRequesterId(@Param("requesterId") Long requesterId); // 요청자 기준 목록
	int existsByCoeatIdAndRequesterId(@Param("coeatId") Long coeatId, @Param("requesterId") Long requesterId); // 중복 여부(XML: COUNT(*))
	int updateStatus(FeedCoeatRequestDTO row); // 승인/거절 등 상태 변경
	int deleteById(@Param("id") Long id); // 삭제(PK)
	int deleteByCoeatId(@Param("coeatId") Long coeatId); // 코잇 삭제 시 일괄 삭제
}
