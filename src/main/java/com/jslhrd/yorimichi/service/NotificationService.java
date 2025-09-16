package com.jslhrd.yorimichi.service;

import java.util.List;

import com.jslhrd.yorimichi.domain.NotificationDTO;

/**
 * 알림 관련 인터페이스 입니다.
 * @author MJ2
 * @since 2025.09.16
 * @note
 */
public interface NotificationService {
	
	/**
	 * 특정 유저(userId) 에 대한 알림을 리스트로 반환합니다.
	 * @param userId
	 * @return List<NotificationDTO>
	 */
	public List<NotificationDTO> findNotificationByUserId(Long userId);

	/**
	 * 알림 정보를 저장합니다.
	 * @param dto
	 */
	public void saveNotification(NotificationDTO dto);

	/**
	 * 알림 아이디 (ntificationId)를 이용해 알림을 읽음 처리합니다.
	 * @param notificationId
	 */
	public void readNotification(Long notificationId);
}
