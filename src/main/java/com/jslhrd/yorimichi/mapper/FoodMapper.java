package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.FoodDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
 * 음식 Mapper.
 *
 * <br>음식 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface FoodMapper {

	/**
	 * 음식 추가.
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(FoodDTO dto);

	// TODO: 상점 기준 음식 목록 조회.

	/**
	 * 음식 단건 조회.
	 * @param id 음식 ID
	 * @return 존재하면 DTO를 담은 Optional, 없으면 Optional.empty()
	 */
	Optional<FoodDTO> selectById(@Param("id") Long id);

	/**
	 * 음식 수정.
	 * @return 영향 행 수 (수정 1, 대상 없음 0)
	 */
	int update(FoodDTO dto);

	/**
	 * 음식 삭제.
	 * @param id 음식 ID
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int deleteById(@Param("id") Long id);
}