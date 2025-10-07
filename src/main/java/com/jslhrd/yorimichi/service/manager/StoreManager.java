package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.SearchDTO;
import com.jslhrd.yorimichi.domain.StoreDTO;
import com.jslhrd.yorimichi.exception.AddressNotFoundException;
import com.jslhrd.yorimichi.exception.BadRequestException;
import com.jslhrd.yorimichi.exception.DuplicateStoreException;
import com.jslhrd.yorimichi.exception.StoreNotFoundException;
import com.jslhrd.yorimichi.mapper.AddressMapper;
import com.jslhrd.yorimichi.mapper.RootMapper;
import com.jslhrd.yorimichi.mapper.StoreMapper;
import com.jslhrd.yorimichi.service.StoreService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class StoreManager implements StoreService {

	private final AddressMapper addressMapper;
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
		return Collections.emptyList();

	}

	@Override
	public List<StoreDTO> findAllByRecommend(Long userId) {
		// TODO: mappers 구현 후 교체
		return Collections.emptyList();
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

		assertActiveAddress(store.getAddressId());

		rootMapper.insert(store);
		if (store.getId() == null) {
			throw new IllegalStateException("Root: insert failed or no generated storeId");
		}

		try {
			storeMapper.insert(store);
		} catch (DuplicateKeyException e) {
			throw new DuplicateStoreException(store.getAddressId(), store.getName());
		}

		log.info("Store: created storeId={}", store.getId());
	}

	@Override
	@Transactional
	public void update(Long storeId, StoreDTO store) {

		if (store.getId() != null && !storeId.equals(store.getId())) {
			throw new BadRequestException("경로의 storeId 와 본문의 id 가 다릅니다.");
		}

		if (store.getAddressId() != null) {
			assertActiveAddress(store.getAddressId());
		}

		try {
			boolean affected = storeMapper.update(storeId, store) > 0;
			if (!affected) {
				assertActiveStore(storeId);
				log.debug("Store: update no-op storeId={}, store={}", storeId, store);
				return;
			}
		} catch (DuplicateKeyException e) {
			throw new DuplicateStoreException(store.getAddressId(), store.getName());
		}

		log.info("Store: updated storeId={}", storeId);
	}

	@Override
	@Transactional
	public void delete(Long storeId) {

		boolean affected = storeMapper.deleteById(storeId) > 0;
		if (!affected) {
			assertActiveStore(storeId);
			return;
		}

		log.info("Store: soft deleted storeId={}", storeId);
	}

	private void assertActiveAddress(Long addressId) {
		boolean exists = addressMapper.existsById(addressId);
		if (!exists) {
			throw new AddressNotFoundException(addressId);
		}
	}

	private void assertActiveStore(Long storeId) {
		boolean exists = storeMapper.existsActive(storeId);
		if (!exists) {
			throw new StoreNotFoundException(storeId);
		}
	}
}
