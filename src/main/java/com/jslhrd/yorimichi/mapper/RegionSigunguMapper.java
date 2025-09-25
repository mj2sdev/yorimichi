package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.RegionSigunguDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
 * 시/군/구 Mapper.
 *
 * <br>시/군/구 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface RegionSigunguMapper {

	/**
	 * 시/군/구 추가.
	 *
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(RegionSigunguDTO dto);

	// TODO: 시/도 기준 시/군/구 목록 조회.

	/**
	 * 시/군/구 단건 조회.
	 *
	 * @param id 시/군/구 ID
	 * @return 존재하면 DTO를 담은 Optional, 없으면 Optional.empty()
	 */
	Optional<RegionSigunguDTO> selectById(@Param("id") Long id);

	/**
	 * 시/군/구 수정.
	 *
	 * @return 영향 행 수 (수정 1, 대상 없음 0)
	 */
	int update(RegionSigunguDTO dto);

	/**
	 * 시/군/구 삭제.
	 *
	 * @param id 시/군/구 ID
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int deleteById(@Param("id") Long id);
}