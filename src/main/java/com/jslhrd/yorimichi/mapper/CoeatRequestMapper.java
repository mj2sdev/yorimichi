package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.CoeatRequestDTO;
import com.jslhrd.yorimichi.enums.CoeatRequestStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

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

	Optional<CoeatRequestStatus> selectStatus(@Param("userId") Long userId,
	                                          @Param("coeatId") Long coeatId);

	int countApprovedById(@Param("coeatId") Long coeatId);

	boolean exists(@Param("userId") Long userId,
	               @Param("coeatId") Long coeatId);

	boolean isApproved(Long userId, Long coeatId);

	/**
	 * 같이먹기 요청 추가.
	 *
	 */
	void insert(CoeatRequestDTO coeatRequest);

	int updateStatus(@Param("userId") Long userId,
	                 @Param("coeatId") Long coeatId,
	                 @Param("from") CoeatRequestStatus from,
	                 @Param("to") CoeatRequestStatus to);

}