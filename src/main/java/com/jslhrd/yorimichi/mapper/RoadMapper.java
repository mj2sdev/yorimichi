package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.RoadDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 도로명 Mapper.
 *
 * <br>도로명 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface RoadMapper {

	/**
	 * 도로명 추가.
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(RoadDTO dto);

	/**
	 * 도로명 단건 조회.
	 * @param id 도로명 ID
	 * @return 존재하면 DTO를 담은 Optional, 없으면 Optional.empty()
	 */
	RoadDTO selectById(@Param("id") Long id);

	/**
	 * 도로명 수정.
	 * @return 영향 행 수 (수정 1, 대상 없음 0)
	 */
	int update(RoadDTO dto);

	/**
	 * 도로명 삭제.
	 * @param id 도로명 ID
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int deleteById(@Param("id") Long id);
}