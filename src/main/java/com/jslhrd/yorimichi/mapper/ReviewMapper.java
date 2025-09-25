package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.ReviewDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
 * 리뷰 Mapper.
 *
 * <br>주소 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface ReviewMapper {

	/**
	 * 주소 추가.
	 *
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(ReviewDTO dto);

	// TODO: 음식 기준 리뷰 목록 조회.

	// TODO: 유저 기준 리뷰 목록 조회.

	/**
	 * 주소 단건 조회.
	 *
	 * @param id 주소 ID
	 * @return 존재하면 DTO를 담은 Optional, 없으면 Optional.empty()
	 */
	Optional<ReviewDTO> selectById(@Param("id") Long id);

	/**
	 * 주소 수정.
	 *
	 * @return 영향 행 수 (수정 1, 대상 없음 0)
	 */
	int update(ReviewDTO dto);

	/**
	 * 주소 삭제.
	 *
	 * @param id 주소 ID
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int deleteById(@Param("id") Long id);
}