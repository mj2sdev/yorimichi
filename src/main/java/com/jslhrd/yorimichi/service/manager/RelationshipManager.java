package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.exception.BadRequestException;
import com.jslhrd.yorimichi.exception.FollowNotFoundException;
import com.jslhrd.yorimichi.exception.ForbiddenException;
import com.jslhrd.yorimichi.exception.UserNotFoundException;
import com.jslhrd.yorimichi.mapper.BlockMapper;
import com.jslhrd.yorimichi.mapper.FollowMapper;
import com.jslhrd.yorimichi.mapper.UserMapper;
import com.jslhrd.yorimichi.service.RelationshipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RelationshipManager implements RelationshipService {

	private final UserMapper userMapper;
	private final FollowMapper followMapper;
	private final BlockMapper blockMapper;

	@Override
	@Transactional(readOnly = true)
	public List<UserDTO> findFollowees(Long followerId) {
		return followMapper.selectFolloweesByUserId(followerId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDTO> findFollowers(Long followeeId) {
		return followMapper.selectFollowersByUserId(followeeId);
	}

	@Override
	public void saveFollow(Long followerId, Long followeeId) {

		if (followerId.equals(followeeId)) {
			throw new BadRequestException("자기 자신은 팔로우할 수 없습니다.");
		}

		assertActiveUser(followerId);
		assertActiveUser(followeeId);

		boolean existsEitherWay = blockMapper.existsEitherWay(followerId, followeeId);
		if (existsEitherWay) {
			throw new ForbiddenException("차단 상태에서는 팔로우할 수 없습니다.");
		}

		try {
			followMapper.insert(followerId, followeeId);
		} catch (DuplicateKeyException e) {
			log.debug("Follow: create no-op followerId={}, followeeId={}", followerId, followeeId);
			return;
		}

		log.info("Follow: created followerId={}, followeeId={}", followerId, followeeId);
	}

	@Override
	public void updateFollowNotification(Long followerId, Long followeeId, boolean notified) {

		boolean affected = followMapper.updateNotification(followerId, followeeId, notified) > 0;
		if (!affected) {
			throw new FollowNotFoundException(followerId, followeeId);
		}

		log.info("Follow: notification updated followerId={}, followeeId={}, notified={}", followerId, followeeId, notified);
	}


	@Override
	public void deleteFollow(Long followerId, Long followeeId) {

		boolean affected = followMapper.delete(followerId, followeeId) > 0;
		if (!affected) {
			log.debug("Follow: delete no-op followerId={}, followeeId={}", followerId, followeeId);
			return;
		}

		log.info("Follow: deleted followerId={}, followeeId={}", followerId, followeeId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDTO> findBlocks(Long userId) {
		return blockMapper.selectAllByUserId(userId);
	}

	@Override
	public void saveBlock(Long blockerId, Long blockeeId) {

		if (blockerId.equals(blockeeId)) {
			throw new BadRequestException("자기 자신은 차단할 수 없습니다.");
		}

		assertActiveUser(blockerId);
		assertActiveUser(blockeeId);

		try {
			blockMapper.insert(blockerId, blockeeId);
		} catch (DuplicateKeyException e) {
			log.debug("Block: no-op blockerId={}, blockeeId={}", blockerId, blockeeId);
			return;
		}

		log.info("Block: created blockerId={}, blockeeId={}", blockerId, blockeeId);

		boolean affected = followMapper.deleteBothDirections(blockerId, blockeeId) > 0;
		log.debug("Block: unfollow both-directions done={}, {}↔{}", affected, blockerId, blockeeId);

		// TODO: (선택) coeat 등 양방향 상호작용 취소/거절도 여기서 처리(멱등)
		// coeatRequestMapper.cancelAllBetween(blockerId, blockeeId);
	}

	@Override
	public void deleteBlock(Long blockerId, Long blockeeId) {

		boolean affected = blockMapper.delete(blockerId, blockeeId) > 0;
		if (!affected) {
			log.debug("Block: delete no-op blockerId={}, blockeeId={}", blockerId, blockeeId);
			return;
		}

		log.info("Block: deleted blockerId={}, blockeeId={}", blockerId, blockeeId);
	}

	private void assertActiveUser(Long userId) {
		boolean exist = userMapper.existsActive(userId);
		if (!exist) {
			throw new UserNotFoundException(userId);
		}
	}
}