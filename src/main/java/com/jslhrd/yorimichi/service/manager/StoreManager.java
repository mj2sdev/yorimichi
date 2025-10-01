package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.SearchDTO;
import com.jslhrd.yorimichi.domain.StoreDTO;
import com.jslhrd.yorimichi.exception.StoreNotFoundException;
import com.jslhrd.yorimichi.mapper.RootMapper;
import com.jslhrd.yorimichi.mapper.StoreMapper;
import com.jslhrd.yorimichi.service.StoreService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class StoreManager implements StoreService {

	private final RootMapper rootMapper;
	private final StoreMapper storeMapper;

	@Override
	public List<StoreDTO> findAll(SearchDTO q) {
		// TODO: 무한 스크롤 구현 후 교체
		return storeMapper.selectAll(q);
	}

	@Override
	public List<StoreDTO> findAllByUserLike(Long userId) {
		// TODO: mappers 구현 후 교체
		throw new UnsupportedOperationException();

	}

	@Override
	public List<StoreDTO> findAllByRecommend(Long userId) {
		// TODO: mappers 구현 후 교체
		throw new UnsupportedOperationException();

	}

	@Override
	public StoreDTO findById(Long storeId) {
		// TODO: review 목록 무한 스크롤 구현 후 교체
		// TODO: coeat 목록 무한 스크롤 구현 후 교체
		return storeMapper.selectById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(storeId));
	}

	@Override
	@Transactional
	public void save(StoreDTO dto) {

		int rootAffected = rootMapper.insert(dto);
		if (rootAffected != 1 || dto.getId() == null) {
			log.warn("Root insert failed or id not generated: rootAffected={}, dto={}", rootAffected, dto);
			throw new IllegalStateException("Root insert failed or no generated id");
		}

		int storeAffected = storeMapper.insert(dto);
		if (storeAffected != 1) {
			log.warn("Store insert failed: storeAffected={}, dto={}", storeAffected, dto);
			throw new IllegalStateException("Store insert failed");
		}

		log.info("Store created id={}", dto.getId());
	}

	@Override
	@Transactional
	public void update(Long storeId, StoreDTO dto) {
		// TODO: address 변경 시 추가 검증/처리
		int affected = storeMapper.update(storeId, dto);

		if (affected != 1) {

			boolean exists = storeMapper.existsById(storeId) == 1;
			if (!exists) {
				throw new StoreNotFoundException(storeId);
			}

			log.info("Store no-op update id={}", storeId);
			return;
		}

		log.info("Store updated id={}", storeId);
	}

	@Override
	@Transactional
	public void delete(Long storeId) {

		int affected = rootMapper.deleteById(storeId);
		if (affected != 1) {
			throw new StoreNotFoundException(storeId);
		}

		log.info("Store deleted id={}", storeId);
	}
}