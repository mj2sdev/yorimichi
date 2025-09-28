package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.ReviewFoodDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 리뷰 - 음식 매핑 Mapper.
 *
 * <br>리뷰 - 음식 매핑 정보를 전달합니다.
 *
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface ReviewFoodMapper {

	/**
	 * 리뷰 - 음식 매핑 추가.
	 *
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(ReviewFoodDTO dto);


	/**
	 * 리뷰 - 음식 매핑 삭제.
	 *
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int delete(ReviewFoodDTO dto);
}