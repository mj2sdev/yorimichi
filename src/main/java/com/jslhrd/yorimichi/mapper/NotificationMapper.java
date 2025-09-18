package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.NotificationDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface NotificationMapper {
	int insert(NotificationDTO dto); // 알림 등록
	NotificationDTO selectById(@Param("id") Long id); // 단건 조회
	List<NotificationDTO> selectByUserId(@Param("userId") Long userId); // 사용자별 알림 목록
	int updateReadAt(@Param("id") Long id); // 읽음 처리
	int deleteById(@Param("id") Long id); // 삭제(PK)
	int deleteByUserId(@Param("userId") Long userId); // 사용자 탈퇴 시 일괄 삭제
}
