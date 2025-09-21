package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.RoleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 권한/역할 Mapper.
 *
 * <br>권한/역할 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface RoleMapper {

	/**
	 * 권한/역할 추가.
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(RoleDTO dto);

	/**
	 * 권한/역할 단건 조회.
	 * @param id 권한/역할 ID
	 * @return 존재하면 DTO를 담은 Optional, 없으면 Optional.empty()
	 */
	RoleDTO selectById(@Param("id") Long id);

	/**
	 * 권한/역할 수정.
	 * @return 영향 행 수 (수정 1, 대상 없음 0)
	 */
	int update(RoleDTO dto);

	/**
	 * 권한/역할 삭제.
	 * @param id 권한/역할 ID
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int deleteById(@Param("id") Long id);
}