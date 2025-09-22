package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.PostalDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
 * 우편번호 Mapper.
 *
 * <br>우편번호 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface PostalMapper {

	/**
	 * 우폅번호 추가.
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(PostalDTO dto);

	/**
	 * 우편번호 단건 조회.
	 * @param id 우편번호 ID
	 * @return 존재하면 DTO를 담은 Optional, 없으면 Optional.empty()
	 */
	Optional<PostalDTO> selectById(@Param("id") Long id);

	/**
	 * 우편번호 수정.
	 * @return 영향 행 수 (수정 1, 대상 없음 0)
	 */
	int update(PostalDTO dto);

	/**
	 * 우편번호 삭제.
	 * @param id 우편번호 ID
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int deleteById(@Param("id") Long id);
}