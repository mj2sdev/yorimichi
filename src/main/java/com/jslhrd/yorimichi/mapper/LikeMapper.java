package com.jslhrd.yorimichi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 좋아요 Mapper.
 *
 * <br>좋아요 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface LikeMapper {

	/**
	 * 좋아요 추가.
	 *
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(@Param("userId") Long userId,@Param("rootId") Long rootId);

	/**
	 * 좋아요 삭제.
	 *
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int delete(@Param("userId") Long userId,@Param("rootId") Long rootId);
}