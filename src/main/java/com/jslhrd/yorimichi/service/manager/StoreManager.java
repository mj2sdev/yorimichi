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
	public void update(StoreDTO dto) {
		// TODO: 주소 변경 시 변경 처리 필요
		int affected = storeMapper.update(dto);
		if (affected == 0) {

			boolean exists = storeMapper.existsById(dto.getId()) == 1;
			if (!exists) {
				throw new StoreNotFoundException(dto.getId());
			}

			log.info("Store no-op update id={}", dto.getId());
			return;
		}

		log.info("Store updated id={}", dto.getId());
	}

	@Override
	@Transactional
	public void delete(Long storeId) {

		int affected = storeMapper.deleteById(storeId);
		if (affected == 0) {
			throw new StoreNotFoundException(storeId);
		}

		log.info("Store deleted id={}", storeId);
	}
}