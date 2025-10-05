package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.CoeatDTO;
import com.jslhrd.yorimichi.domain.CoeatRequestDTO;
import com.jslhrd.yorimichi.exception.CoeatNotFoundException;
import com.jslhrd.yorimichi.exception.DuplicateCoeatRequestException;
import com.jslhrd.yorimichi.exception.StoreNotFoundException;
import com.jslhrd.yorimichi.mapper.CoeatMapper;
import com.jslhrd.yorimichi.mapper.CoeatRequestMapper;
import com.jslhrd.yorimichi.mapper.RootMapper;
import com.jslhrd.yorimichi.mapper.StoreMapper;
import com.jslhrd.yorimichi.service.CoeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CoeatManager implements CoeatService {

	private final RootMapper rootMapper;
	private final StoreMapper storeMapper;
	private final CoeatMapper coeatMapper;
	private final CoeatRequestMapper coeatRequestMapper;


	@Override
	@Transactional(readOnly = true)
	public List<CoeatDTO> findAll() {
		// TODO: 무한 스크룰 및 coeat 상세 정보 추후 구현
		return coeatMapper.selectAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<CoeatDTO> findAllByStoreId(Long storeId) {
		// TODO: 무한 스크룰 및 coeat 상세 정보 추후 구현
		return coeatMapper.selectAllByStoreId(storeId);
	}

	@Override
	@Transactional(readOnly = true)
	public CoeatDTO findById(Long coeatId) {
		return coeatMapper.selectById(coeatId)
				.orElseThrow(() -> new CoeatNotFoundException(coeatId));
	}

	@Override
	public void save(Long userId, Long storeId, CoeatDTO coeat) {

		boolean exists = storeMapper.existsActive(storeId);
		if (!exists) {
			throw new StoreNotFoundException(storeId);
		}

		coeat.setUserId(userId);
		coeat.setStoreId(storeId);

		boolean affectedRoot = rootMapper.insert(coeat) > 0;
		if (!affectedRoot || coeat.getId() == null) {
			log.warn("Root insert failed or coeatId not generated: affectedRoot={}, coeat={}", affectedRoot, coeat);
			throw new IllegalStateException("Root insert failed or no generated coeatId");
		}

		boolean affectedCoeat = coeatMapper.insert(coeat) > 0;
		if (!affectedCoeat) {
			log.warn("Coeat insert failed: affectedCoeat={}, coeat={}", affectedCoeat, coeat);
			throw new IllegalStateException("Coeat insert failed");
		}

		log.info("Coeat created coeatId={}", coeat.getId());
	}

	@Override
	public void update(Long userId, Long coeatId, CoeatDTO coeat) {

		boolean affected = coeatMapper.update(userId, coeatId, coeat) > 0;
		if (affected) {
			log.info("Coeat updated coeatId={}", coeatId);
			return;
		}

		boolean exists = coeatMapper.existsActive(coeatId);
		if (!exists) {
			throw new CoeatNotFoundException(coeatId);
		}

		throw new AccessDeniedException("같이먹기 수정 권한이 없습니다.");
	}

	@Override
	public void delete(Long userId, Long coeatId) {

		boolean affected = coeatMapper.deleteById(userId, coeatId) > 0;
		if (affected) {
			log.info("Coeat soft deleted coeatId={}", coeatId);
			return;
		}

		boolean exists = coeatMapper.existsActive(coeatId);
		if (!exists) {
			throw new CoeatNotFoundException(coeatId);
		}

		throw new AccessDeniedException("같이먹기 삭제 권한이 없습니다.");
	}


	@Override
	public void saveCoeatRequest(Long userId, Long coeatId, CoeatRequestDTO coeatRequest) {

		boolean exists = coeatMapper.existsActive(coeatId);
		if (!exists) {
			throw new CoeatNotFoundException(coeatId);
		}

		boolean isOwner = coeatMapper.isOwner(userId, coeatId);
		if (isOwner) {
			throw new IllegalStateException("작성자는 신청할 수 없습니다.");
		}

		coeatRequest.setUserId(userId);
		coeatRequest.setCoeatId(coeatId);

		try {
			boolean affected = coeatRequestMapper.insert(coeatRequest) > 0;
			if (!affected) {
				log.warn("CoeatRequest insert failed: coeatRequestAffected={}, coeat={}", affected, coeatRequest);
				throw new IllegalStateException("CoeatRequest insert failed");
			}
		} catch (DataIntegrityViolationException e) {
			throw new DuplicateCoeatRequestException(userId, coeatId);
		}

		log.info("CoeatRequest created userId={}, coeatId={}", userId, coeatId);
	}

	@Override
	public void updateCoeatRequestStatus(Long ownerId, Long coeatId, CoeatRequestDTO coeatRequest) {

		boolean isOwner = coeatMapper.isOwner(ownerId, coeatId);
		if (!isOwner) {
			throw new AccessDeniedException("작성자만 승인/거절이 가능합니다.");
		}

		boolean affected = coeatRequestMapper.updateStatusByOwner(coeatId, coeatRequest) > 0;
		if (affected) {
			log.info("CoeatRequest status updated requestUserId={}, coeatId={}, to={}", coeatRequest.getUserId(), coeatId, coeatRequest.getStatus());
			return;
		}

		throw new AccessDeniedException("같이먹기 신청 상태를 변경 할 수 없습니다.");
	}

	@Override
	public void cancelCoeatRequest(Long userId, Long coeatId) {

		boolean affected = coeatRequestMapper.cancelByRequester(userId, coeatId) > 0;
		if (affected) {
			log.info("CoeatRequest cancelled userId={}, coeatId={}", userId, coeatId);
			return;
		}

		throw new AccessDeniedException("같이먹기 신청 취소 권한이 없습니다.");
	}
}