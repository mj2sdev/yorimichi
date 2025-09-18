package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.LikeDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LikeMapper {

	/* 좋아요 생성 */
	int insert(LikeDTO dto);

	/* 특정 피드의 좋아요 목록(필요 시) */
	List<LikeDTO> selectByFeedId(@Param("feedId") Long feedId);

	/* 사용자+피드 조합 존재 여부 - XML에서 COUNT(*) */
	int existsByUserIdAndFeedId(@Param("userId") Long userId, @Param("feedId") Long feedId);

	/* 피드별 좋아요 수 - XML에서 COUNT(*) */
	int countByFeedId(@Param("feedId") Long feedId);

	/* 단건 삭제(PK) */
	int deleteById(@Param("id") Long id);

	/* 사용자+피드 조합으로 삭제(좋아요 취소) */
	int deleteByUserIdAndFeedId(@Param("userId") Long userId, @Param("feedId") Long feedId);
}
