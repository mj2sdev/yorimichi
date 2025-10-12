package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.ImageDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
 * 이미지 Mapper.
 *
 * <br>이미지 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface ImageMapper {

	/**
	 * 이미지 단건 조회.
	 *
	 * @param imageId 이미지 ID
	 * @return 존재하면 DTO를 담은 Optional, 없으면 Optional.empty()
	 */
	Optional<ImageDTO> selectById(@Param("imageId") Long imageId);

	boolean existsById(@Param("imageId") Long imageId);

	/**
	 * 이미지 추가.
	 *
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(ImageDTO image);

	/**
	 * 이미지 수정.
	 *
	 * @return 영향 행 수 (수정 1, 대상 없음 0)
	 */
	int update(@Param("imageId") Long imageId,
	           @Param("image") ImageDTO image);

	/**
	 * 이미지 삭제.
	 *
	 * @param imageId 이미지 ID
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int deleteById(@Param("imageId") Long imageId);
}