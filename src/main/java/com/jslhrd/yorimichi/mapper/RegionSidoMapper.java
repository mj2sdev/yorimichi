package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.RegionSidoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
 * 시/도 Mapper.
 *
 * <br>시/도 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface RegionSidoMapper {

	/**
	 * 시/도 추가.
	 *
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(RegionSidoDTO dto);

	// TODO: 시/도 목록 조회.

	/**
	 * 시/도 단건 조회.
	 *
	 * @param id 시/도 ID
	 * @return 존재하면 DTO를 담은 Optional, 없으면 Optional.empty()
	 */
	Optional<RegionSidoDTO> selectById(@Param("id") Long id);

	/**
	 * 시/도 수정.
	 *
	 * @return 영향 행 수 (수정 1, 대상 없음 0)
	 */
	int update(RegionSidoDTO dto);

	/**
	 * 시/도 삭제.
	 *
	 * @param id 시/도 ID
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int deleteById(@Param("id") Long id);
}