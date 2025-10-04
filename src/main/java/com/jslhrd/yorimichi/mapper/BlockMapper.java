package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 차단 Mapper.
 *
 * <br>차단 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface BlockMapper {

	List<UserDTO> selectAllByUserId(@Param("userId") Long userId);

	boolean exists(@Param("blockerId") Long blockerId, @Param("blockeeId") Long blockeeId);

	/**
	 * 차단 추가.
	 *
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(@Param("blockerId") Long blockerId, @Param("blockeeId") Long blockeeId);

	/**
	 * 차단 삭제.
	 *
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int delete(@Param("blockerId") Long blockerId, @Param("blockeeId") Long blockeeId);
}