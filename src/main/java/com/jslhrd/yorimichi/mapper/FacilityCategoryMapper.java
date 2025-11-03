package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.FacilityCategoryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 시설 카테고리 Mapper.
 *
 * <br>시설 카테고리 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface FacilityCategoryMapper {

	List<FacilityCategoryDTO> selectAll();

	/**
	 * 시설 카테고리 단건 조회.
	 *
	 * @param facilityId 시설 카테고리 ID
	 * @return 존재하면 DTO를 담은 Optional, 없으먄 Optional.empty()
	 */
	Optional<FacilityCategoryDTO> selectById(@Param("facilityId") Long facilityId);

	Long selectIdByName(@Param("name") String name);

	boolean existsById(@Param("facilityId") Long facilityId);

	/**
	 * 시설 카테고리 추가.
	 *
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(FacilityCategoryDTO facility);

	/**
	 * 시설 카테고리 수정.
	 *
	 * @return 영향 행 수 (수정 1, 대상 없음 0)
	 */
	int update(@Param("facilityId") Long facilityId,
	           @Param("facility") FacilityCategoryDTO facility);

	/**
	 * 시설 카테고리 삭제.
	 *
	 * @param facilityId 시설 카테고리 ID
	 * @return 영향 행 수 (삭제 1, 대상 없음0)
	 */
	int deleteById(@Param("facilityId") Long facilityId);
}