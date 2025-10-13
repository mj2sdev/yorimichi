package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.NotificationDTO;
import com.jslhrd.yorimichi.exception.ForbiddenException;
import com.jslhrd.yorimichi.exception.NotificationNotFoundException;
import com.jslhrd.yorimichi.exception.RootNotFoundException;
import com.jslhrd.yorimichi.exception.UserNotFoundException;
import com.jslhrd.yorimichi.mapper.NotificationMapper;
import com.jslhrd.yorimichi.mapper.RootMapper;
import com.jslhrd.yorimichi.mapper.UserMapper;
import com.jslhrd.yorimichi.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NotificationManager implements NotificationService {

	private final RootMapper rootMapper;
	private final UserMapper userMapper;
	private final NotificationMapper notificationMapper;

	@Override
	@Transactional(readOnly = true)
	public List<NotificationDTO> findAllByUserId(Long userId) {
		return notificationMapper.selectByUserId(userId);
	}

	@Override
	public void save(NotificationDTO notification) {

		assertActiveRoot(notification.getRootId());
		assertActiveUser(notification.getTargetUserId());

		notificationMapper.insert(notification);
		log.info("Notification: created notificationId={}", notification.getId());
	}

	@Override
	public void read(Long userId, Long notificationId) {

		boolean affected = notificationMapper.update(userId, notificationId) > 0;
		if (!affected) {
			assertActiveNotification(notificationId);
			boolean isOwner = notificationMapper.isOwner(userId, notificationId);
			if (!isOwner) {
				throw new ForbiddenException("다른 사용자의 알림입니다.");
			}
			log.debug("Notification: read no-op notificationId={}", notificationId);
			return;
		}

		log.info("Notification: read notificationId={}", notificationId);
	}

	private void assertActiveRoot(Long rootId) {
		boolean exists = rootMapper.existsActive(rootId);
		if (!exists) {
			throw new RootNotFoundException(rootId);
		}
	}

	private void assertActiveUser(Long userId) {
		boolean exists = userMapper.existsActive(userId);
		if (!exists) {
			throw new UserNotFoundException(userId);
		}
	}

	private void assertActiveNotification(Long notificationId) {
		boolean exists = notificationMapper.existsById(notificationId);
		if (!exists) {
			throw new NotificationNotFoundException(notificationId);
		}
	}

}