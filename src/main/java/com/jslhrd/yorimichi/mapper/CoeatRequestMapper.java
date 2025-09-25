package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.CoeatRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 같이먹기 요청 Mapper.
 *
 * <br>같이먹기 요청 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface CoeatRequestMapper {

	/**
	 * 주소 추가.
	 *
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(CoeatRequestDTO dto);

	// TODO: 같이먹기 기준 요청 목록 조회.

	// TODO: 요청 처리.

	/**
	 * 같이먹기 요청 삭제.
	 *
	 * @param id 같이먹기 요청 ID
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int deleteById(@Param("id") Long id);
}