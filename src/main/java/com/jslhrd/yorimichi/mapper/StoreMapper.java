package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.SearchDTO;
import com.jslhrd.yorimichi.domain.StoreDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 상점 Mapper.
 *
 * <br>상점 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface StoreMapper {

	/**
	 * 상점 추가.
	 *
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(StoreDTO dto);

	int countAll(@Param("q") SearchDTO q);

	List<StoreDTO> selectAll(@Param("q") SearchDTO q);

	/**
	 * 상점 단건 조회.
	 *
	 * @param id 상점 ID
	 * @return 존재하면 DTO를 담은 Optional, 없으면 Optional.empty()
	 */
	Optional<StoreDTO> selectById(@Param("id") Long id);

	int existsById(Long id);

	/**
	 * 상점 수정.
	 *
	 * @return 영향 행 수 (수정 1, 대상 없음 0)
	 */
	int update(StoreDTO dto);

	/**
	 * 상점 삭제.
	 *
	 * @param id 상점 ID
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int deleteById(@Param("id") Long id);
}