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
	public List<StoreDTO> findAll(SearchDTO search) {
		// TODO: 무한 스크롤 구현 후 교체
		return storeMapper.selectAll(search);
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
	public void save(StoreDTO store) {

		boolean affectedRoot = rootMapper.insert(store) > 0;
		if (!affectedRoot || store.getId() == null) {
			log.warn("Root insert failed or storeId not generated: affectedRoot={}, store={}", affectedRoot, store);
			throw new IllegalStateException("Root insert failed or no generated storeId");
		}

		boolean affectedStore = storeMapper.insert(store) > 0;
		if (affectedStore) {
			log.warn("Store insert failed: affectedStore={}, store={}", affectedStore, store);
			throw new IllegalStateException("Store insert failed");
		}

		log.info("Store created storeId={}", store.getId());
	}

	@Override
	@Transactional
	public void update(Long storeId, StoreDTO store) {

		// TODO: address 변경 시 추가 검증/처리
		boolean affected = storeMapper.update(storeId, store) > 0;
		if (affected) {
			log.info("Store updated storeId={}", storeId);
			return;
		}

		boolean exists = storeMapper.existsActive(storeId);
		if (!exists) {
			throw new StoreNotFoundException(storeId);
		}
	}

	@Override
	@Transactional
	public void delete(Long storeId) {

		boolean affected = storeMapper.deleteById(storeId) > 0;
		if (affected) {
			log.info("Store soft deleted storeId={}", storeId);
			return;
		}

		boolean exists = storeMapper.existsActive(storeId);
		if (!exists) {
			throw new StoreNotFoundException(storeId);
		}
	}
}