package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.StoreFacilityCategoryDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 상점 - 시설 카테고리 매핑 Mapper.
 *
 * <br>상점 - 시설 카테고리 매핑 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface StoreFacilityCategoryMapper {

	/**
	 * 상점 - 시설 카테고리 매핑 추가.
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(StoreFacilityCategoryDTO dto);

	/**
	 * 상점 - 시설 카테고리 매핑 삭제.
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int delete(StoreFacilityCategoryDTO dto);
}