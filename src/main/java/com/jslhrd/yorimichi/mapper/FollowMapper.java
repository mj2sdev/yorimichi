package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 팔로우 Mapper.
 *
 * <br>팔로우 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface FollowMapper {

	List<UserDTO> selectFollowersByUserId(@Param("userId") Long userId);

	List<UserDTO> selectFolloweesByUserId(@Param("userId") Long userId);

	boolean exists(@Param("followerId") Long followerId,
	               @Param("followeeId") Long followeeId);

	/**
	 * 팔로우 생성.
	 *
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(@Param("followerId") Long followerId,
	           @Param("followeeId") Long followeeId);

	int updateNotification(@Param("followerId") Long followerId,
	                       @Param("followeeId") Long followeeId,
	                       @Param("notified") boolean notified);

	/**
	 * 팔로우 삭제.
	 *
	 * @return 영향 생 수 (삭제 1, 대상 없음 0)
	 */
	int delete(@Param("followerId") Long followerId,
	           @Param("followeeId") Long followeeId);

	int deleteBothDirections(@Param("followerId") Long followerId,
	                         @Param("followeeId") Long followeeId);
}