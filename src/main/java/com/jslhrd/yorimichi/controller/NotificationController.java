package com.jslhrd.yorimichi.controller;

import com.jslhrd.yorimichi.domain.NotificationDTO;
import com.jslhrd.yorimichi.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationService notificationService;

	//특정 유저의 알림목록을 불러오는 메서드.
	@GetMapping("/notification")
	public List<NotificationDTO> getNotifications(Principal principal) {
		Long userId = (long) 1234;
		return notificationService.findAllByUserId(userId);
		//return notificationService.findAllByUserId(principal);
	}

	//알림을 읽으면 읽음처리 하는 메서드.
	@PatchMapping("/notification/{id}")
	public void updateNotificationStatus(
			Long userId,
			@PathVariable("id") Long notificationId) {
		notificationService.read(userId, notificationId);
	}

}
