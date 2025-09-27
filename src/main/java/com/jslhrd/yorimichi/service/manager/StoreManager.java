package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.SearchDTO;
import com.jslhrd.yorimichi.domain.StoreDTO;
import com.jslhrd.yorimichi.exception.StoreNotFoundException;
import com.jslhrd.yorimichi.mapper.StoreMapper;
import com.jslhrd.yorimichi.service.StoreService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Slf4j
@Service
@Validated
@AllArgsConstructor
@Transactional(readOnly = true)
public class StoreManager implements StoreService {

	private final StoreMapper storeMapper;

	@Override
	public List<StoreDTO> findAll(@Valid SearchDTO dto) {

		// TODO: mapper 페이징 구현 필요
		List<StoreDTO> stores = storeMapper.selectAll(dto);

		return stores;
	}

	@Override
	public List<StoreDTO> findAllByUserLike(@NotNull @Min(1) Long userId) {

		// TODO: mapper 구현 후 교체
		throw new UnsupportedOperationException();

	}

	@Override
	public List<StoreDTO> findAllByRecommend(@NotNull @Min(1) Long userId) {

		// TODO: mapper 구현 후 교체
		throw new UnsupportedOperationException();

	}

	@Override
	public StoreDTO findById(@NotNull @Min(1) Long storeId) {
		return storeMapper.selectById(storeId)
				.orElseThrow(() -> new StoreNotFoundException(storeId));
	}

	@Override
	@Transactional
	public void save(@Validated(Create.class) StoreDTO dto) {

		int result = storeMapper.insert(dto);

		if (result == 0) {
			log.warn("Store insert affectedResult={} dto={}", result, dto);
			throw new IllegalStateException("Store insert failed. result=" + result + ", dto=" + dto);
		}

		log.info("Store created id={}", dto.getId());
	}

	@Override
	@Transactional
	public void update(@Validated(Create.class) StoreDTO dto) {

		int result = storeMapper.update(dto);

		if (result == 0) {
			throw new StoreNotFoundException(dto.getId());
		}

		log.info("Store updated id={}", dto.getId());
	}

	@Override
	@Transactional
	public void delete(@NotNull @Min(1) Long storeId) {

		int result = storeMapper.deleteById(storeId);

		if (result == 0) {
			throw new StoreNotFoundException(storeId);
		}

		log.info("Store deleted id={}", storeId);
	}
}