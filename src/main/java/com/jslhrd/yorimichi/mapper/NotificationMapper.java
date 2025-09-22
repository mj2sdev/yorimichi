package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.NotificationDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
 * 알림 Mapper.
 *
 * <br>알림 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface NotificationMapper {

	/**
	 * 알림 추가.
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(NotificationDTO dto); // 알림 등록
	
	// TODO: 수신자 기준 알림 목록 조회.

	/**
	 * 알림 단건 조회.
	 * @param id 알림 ID
	 * @return 존재하면 DTO를 담은 Optional, 없으면 Optional.empty()
	 */
	Optional<NotificationDTO> selectById(@Param("id") Long id);

	/**
	 * 알림 삭제 (읽음).
	 * @param id 주소 ID
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int deleteById(@Param("id") Long id);
}