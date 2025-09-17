package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.FollowDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FollowMapper {

	/* 팔로우 생성 */
	int insert(FollowDTO follow);

	/* 특정 유저의 팔로워 목록 */
	List<FollowDTO> selectFollowersByUserId(@Param("userId") Long userId);

	/* 특정 유저가 팔로우하는(팔로잉) 목록 */
	List<FollowDTO> selectFollowingsByUserId(@Param("userId") Long userId);

	/* 페어 존재 여부(중복체크) - XML에서는 COUNT(*) 사용 */
	int existsByPair(@Param("followerId") Long followerId, @Param("followingId") Long followingId);

	/* 단건 삭제(PK) */
	int deleteById(@Param("id") Long id);

	/* 페어 기준 삭제(언팔로우) */
	int deleteByPair(@Param("followerId") Long followerId, @Param("followingId") Long followingId);
}
