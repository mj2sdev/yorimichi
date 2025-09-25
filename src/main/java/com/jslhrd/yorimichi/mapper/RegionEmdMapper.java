package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.RegionEmdDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
 * 읍/면/동 Mapper.
 *
 * <br>읍/면/동 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface RegionEmdMapper {

	/**
	 * 읍/면/동 추가.
	 *
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(RegionEmdDTO dto);

	/**
	 * 읍/면/동 단건 조회.
	 *
	 * @param id 읍/면/동 ID
	 * @return 존재하면 DTO를 담은 Optional, 없으면 Optional.empty()
	 */
	Optional<RegionEmdDTO> selectById(@Param("id") Long id);

	/**
	 * 읍/면/동 수정.
	 *
	 * @return 영향 행 수 (수정 1, 대상 없음 0)
	 */
	int update(RegionEmdDTO dto);

	/**
	 * 읍/면/동 삭제.
	 *
	 * @param id 읍/면/동 ID
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int deleteById(@Param("id") Long id);
}