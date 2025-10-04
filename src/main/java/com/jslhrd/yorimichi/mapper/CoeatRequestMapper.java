package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.CoeatRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 같이먹기 요청 Mapper.
 *
 * <br>같이먹기 요청 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface CoeatRequestMapper {

	boolean exists(@Param("userId") Long userId,
	               @Param("id") Long coeatId);

	/**
	 * 같이먹기 요청 추가.
	 *
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(CoeatRequestDTO dto);

	int updateStatusByOwner(@Param("coeatId") Long coeatId,
	                        @Param("dto") CoeatRequestDTO dto);

	/**
	 * 같이먹기 요청 삭제.
	 *
	 * @param userId  유저 ID
	 * @param coeatId 같이먹기 ID
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int cancelByRequester(@Param("userId") Long userId,
	                      @Param("coeatId") Long coeatId);
}