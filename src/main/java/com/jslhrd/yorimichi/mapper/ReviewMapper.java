package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.ReviewDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 리뷰 Mapper.
 *
 * <br>리뷰 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface ReviewMapper {

	List<ReviewDTO> selectAll();

	List<ReviewDTO> selectAllByStoreId(@Param("storeId") Long storeId);

	/**
	 * 리뷰 단건 조회.
	 *
	 * @param id 리뷰 ID
	 * @return 존재하면 DTO를 담은 Optional, 없으면 Optional.empty()
	 */
	Optional<ReviewDTO> selectById(@Param("id") Long id);

	boolean existsActive(@Param("id") Long id);

	/**
	 * 리뷰 추가.
	 *
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(ReviewDTO dto);

	/**
	 * 리뷰 수정.
	 *
	 * @param userId 유저 ID
	 * @param id     리뷰 ID
	 * @param dto    수정된 DTO
	 * @return 영향 행 수 (수정 1, 대상 없음 0)
	 */
	int update(@Param("userId") Long userId,
	           @Param("id") Long id,
	           @Param("dto") ReviewDTO dto);

	/**
	 * 리뷰 삭제.
	 *
	 * @param userId 유저 ID
	 * @param id     리뷰 ID
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int deleteById(@Param("userId") Long userId,
	               @Param("id") Long id);
}