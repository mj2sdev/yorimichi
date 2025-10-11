package com.jslhrd.yorimichi.exception;

import java.util.Map;

public class NotificationNotFoundException extends DomainException {

	public NotificationNotFoundException(Long notificationId) {
		super(
				"NOTIFICATION_NOT_FOUND",
				"알림을 찾을 수 없습니다.",
				Map.of("notificationId", notificationId)
		);
	}
}