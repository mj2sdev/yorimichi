package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.CoeatDTO;
import com.jslhrd.yorimichi.domain.CoeatRequestDTO;
import com.jslhrd.yorimichi.enums.CoeatRequestStatus;
import com.jslhrd.yorimichi.exception.*;
import com.jslhrd.yorimichi.mapper.*;
import com.jslhrd.yorimichi.service.CoeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.jslhrd.yorimichi.enums.CoeatRequestStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CoeatManager implements CoeatService {

	private final RootMapper rootMapper;
	private final StoreMapper storeMapper;
	private final UserMapper userMapper;
	private final CoeatMapper coeatMapper;
	private final CoeatRequestMapper coeatRequestMapper;


	@Override
	@Transactional(readOnly = true)
	public List<CoeatDTO> findAll() {
		// TODO: 무한 스크룰 및 coeat 리스트 정보 추후 구현
		return coeatMapper.selectAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<CoeatDTO> findAllByStoreId(Long storeId) {
		// TODO: 무한 스크룰 및 coeat 리스트 정보 추후 구현
		return coeatMapper.selectAllByStoreId(storeId);
	}

	@Override
	@Transactional(readOnly = true)
	public CoeatDTO findById(Long coeatId) {
		// TODO: coeat 상세 정보 추후 구현
		return coeatMapper.selectById(coeatId)
				.orElseThrow(() -> new CoeatNotFoundException(coeatId));
	}

	@Override
	public void save(Long userId, Long storeId, CoeatDTO coeat) {

		assertActiveUser(userId);
		assertActiveStore(storeId);

		coeat.setUserId(userId);
		coeat.setStoreId(storeId);

		rootMapper.insert(coeat);
		if (coeat.getId() == null) {
			throw new IllegalStateException("Root: insert failed or no generated coeatId");
		}

		coeatMapper.insert(coeat);
		log.info("Coeat: created coeatId={}", coeat.getId());
	}

	@Override
	public void update(Long userId, Long coeatId, CoeatDTO coeat) {

		if (coeat.getId() != null && !coeatId.equals(coeat.getId())) {
			throw new BadRequestException("경로의 coeatId 와 본문의 id 가 다릅니다.");
		}

		boolean affected = coeatMapper.update(userId, coeatId, coeat) > 0;
		if (!affected) {
			assertActiveCoeat(coeatId);
			throw new ForbiddenException("같이먹기 수정 권한이 없습니다.");
		}

		log.info("Coeat: updated coeatId={}", coeatId);
	}

	@Override
	public void delete(Long userId, Long coeatId) {

		boolean affected = coeatMapper.deleteById(userId, coeatId) > 0;
		if (!affected) {
			assertActiveCoeat(coeatId);
			throw new ForbiddenException("같이먹기 삭제 권한이 없습니다.");
		}

		log.info("Coeat: soft deleted coeatId={}", coeatId);
	}


	@Override
	public void saveCoeatRequest(Long requesterId, Long coeatId, CoeatRequestDTO coeatRequest) {

		assertActiveUser(requesterId);
		assertActiveCoeat(coeatId);

		boolean isOwner = coeatMapper.isOwner(requesterId, coeatId);
		if (isOwner) {
			throw new BadRequestException("작성자는 신청할 수 없습니다.");
		}

		boolean isOpen = coeatMapper.isOpen(coeatId);
		if (!isOpen) {
			throw new InvalidStateException("모집 중이 아닙니다.");
		}

		coeatRequest.setUserId(requesterId);
		coeatRequest.setCoeatId(coeatId);

		try {
			coeatRequestMapper.insert(coeatRequest);
		} catch (DuplicateKeyException e) {
			throw new DuplicateCoeatRequestException(requesterId, coeatId);
		}

		log.info("CoeatRequest: created requesterId={}, coeatId={}", requesterId, coeatId);
	}

	@Override
	public void updateCoeatRequestStatus(Long ownerId, Long coeatId, CoeatRequestDTO coeatRequest) {

		boolean isOwner = coeatMapper.isOwner(ownerId, coeatId);
		if (!isOwner) {
			throw new ForbiddenException("작성자만 승인/거절 가능합니다.");
		}

		boolean isOpen = coeatMapper.isOpen(coeatId);
		if (!isOpen) {
			throw new InvalidStateException("모집 중이 아닙니다.");
		}

		Long requesterId = coeatRequest.getUserId();

		CoeatRequestStatus to = coeatRequest.getStatus();
		CoeatRequestStatus cur = findStatus(requesterId, coeatId);

		if (!(cur == PENDING && (to == APPROVED || to == REJECTED))) {
			throw new BadRequestException("허용되지 않은 전이");
		}

		if (to == APPROVED) {
			int capacity = coeatMapper.selectCapacityById(coeatId);
			int approved = coeatRequestMapper.countApprovedById(coeatId);
			if (approved >= capacity) {
				throw new ConflictException("정원 초과");
			}
		}

		boolean affected = coeatRequestMapper.updateStatus(requesterId, coeatId, cur, to) > 0;
		if (!affected) {
			CoeatRequestStatus after = findStatus(requesterId, coeatId);
			if (after == to) {
				log.debug("CoeatRequest: status update no-op requesterId={}, coeatId={}", requesterId, coeatId);
				return;
			}
			if (after == cur) {
				throw new ConflictException("동시 승인 충돌");
			}
			throw new BadRequestException("허용되지 않은 전이");
		}

		log.info("CoeatRequest: status updated requesterId={}, coeatId={}, to={}",
				requesterId, coeatId, to);
	}

	@Override
	public void cancelCoeatRequest(Long requesterId, Long coeatId) {

		boolean isRequester = coeatRequestMapper.isRequester(requesterId, coeatId);
		if (!isRequester) {
			throw new ForbiddenException("본인만 취소할 수 있습니다.");
		}

		CoeatRequestStatus cur = findStatus(requesterId, coeatId);
		if (cur == CANCELLED || cur == REJECTED) {
			throw new BadRequestException("현재 상태에서는 취소할 수 없습니다.");
		}

		boolean affected = coeatRequestMapper.updateStatus(requesterId, coeatId, cur, CANCELLED) > 0;
		if (!affected) {
			CoeatRequestStatus after = findStatus(requesterId, coeatId);
			if (after == CANCELLED) {
				log.debug("CoeatRequest: cancel no-op requesterId={}, coeatId={}", requesterId, coeatId);
				return;
			}
			if (after == cur) {
				throw new ConflictException("동시 상태 변경 충돌");
			}
			throw new BadRequestException("허용되지 않은 전이");
		}

		log.info("CoeatRequest: cancelled requesterId={}, coeatId={}", requesterId, coeatId);
	}


	private void assertActiveStore(Long storeId) {
		boolean exists = storeMapper.existsActive(storeId);
		if (!exists) {
			throw new StoreNotFoundException(storeId);
		}
	}

	private void assertActiveUser(Long userId) {
		boolean existsUser = userMapper.existsActive(userId);
		if (!existsUser) {
			throw new UserNotFoundException(userId);
		}
	}

	private void assertActiveCoeat(Long coeatId) {
		boolean exists = coeatMapper.existsActive(coeatId);
		if (!exists) {
			throw new CoeatNotFoundException(coeatId);
		}
	}

	private CoeatRequestStatus findStatus(Long requesterId, Long coeatId) {
		return coeatRequestMapper.selectStatus(requesterId, coeatId)
				.orElseThrow(() -> new CoeatRequestNotFoundException(requesterId, coeatId));
	}
}
