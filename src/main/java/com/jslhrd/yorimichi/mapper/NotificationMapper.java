package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.NotificationDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface NotificationMapper {
    int insert(NotificationDTO notification); // 알림 생성
    NotificationDTO selectById(Long notificationId); // 알림 조회
    List<NotificationDTO> listByTargetUserId(Long targetUserId); // 특정 수신자의 알림 목록
    int markRead(Long notificationId); // 읽음 처리
    int update(NotificationDTO notification); // 메세지 / 갱신 등 일반 업데이트
    int deleteById(Long notificationId); // 알림 삭제
}
