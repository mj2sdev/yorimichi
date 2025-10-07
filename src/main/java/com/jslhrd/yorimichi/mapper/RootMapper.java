package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.RootDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 루트 Mapper.
 *
 * <br>루트 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface RootMapper {

	/**
	 * 루트 추가.
	 *
	 */
	void insert(RootDTO dto);

	/**
	 * 루트 삭제.
	 *
	 * @param rootId 루트 ID
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int deleteById(@Param("rootId") Long rootId);
}