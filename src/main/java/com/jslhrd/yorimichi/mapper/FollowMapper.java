package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.FollowDTO;
import org.apache.ibatis.annotations.Mapper;

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

	/**
	 * 팔로우 생성.
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(FollowDTO dto);

	/**
	 * 팔로우 삭제.
	 * @return 영향 생 수 (삭제 1, 대상 없음 0)
	 */
	int delete(FollowDTO dto);
}