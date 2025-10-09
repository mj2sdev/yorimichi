package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.FoodDTO;
import com.jslhrd.yorimichi.exception.BadRequestException;
import com.jslhrd.yorimichi.exception.DuplicateFoodException;
import com.jslhrd.yorimichi.exception.FoodNotFoundException;
import com.jslhrd.yorimichi.exception.StoreNotFoundException;
import com.jslhrd.yorimichi.mapper.FoodMapper;
import com.jslhrd.yorimichi.mapper.RootMapper;
import com.jslhrd.yorimichi.mapper.StoreMapper;
import com.jslhrd.yorimichi.service.FoodService;
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
public class FoodManager implements FoodService {

	private final RootMapper rootMapper;
	private final StoreMapper storeMapper;
	private final FoodMapper foodMapper;

	@Override
	@Transactional(readOnly = true)
	public List<FoodDTO> findAll() {
		return foodMapper.selectAll();
	}

	@Override
	public void save(FoodDTO food) {

		assertActiveStore(food.getStoreId());

		rootMapper.insert(food);
		if (food.getId() == null) {
			throw new IllegalStateException("Root: insert failed or no generated foodId");
		}

		try {
			foodMapper.insert(food);
		} catch (DuplicateKeyException e) {
			throw new DuplicateFoodException(food.getStoreId(), food.getName());
		}

		log.info("Food: created foodId={}", food.getId());
	}

	@Override
	public void update(Long storeId, Long foodId, FoodDTO food) {

		if (food.getId() != null && !foodId.equals(food.getId())) {
			throw new BadRequestException("경로의 foodId 와 본문의 id 가 다릅니다.");
		}

		if (food.getStoreId() != null && !storeId.equals(food.getStoreId())) {
			throw new BadRequestException("food 의 storeId 는 수정할 수 없습니다.");
		}

		try {
			boolean affected = foodMapper.update(storeId, foodId, food) > 0;
			if (!affected) {
				assertActiveFood(foodId);
				log.debug("Food: update no-op foodId={}, food={}", foodId, food);
				return;
			}
		} catch (DuplicateKeyException e) {
			throw new DuplicateFoodException(food.getStoreId(), food.getName());
		}

		log.info("Food: updated foodId={}", foodId);
	}

	@Override
	public void delete(Long storeId, Long foodId) {

		boolean affected = foodMapper.deleteById(storeId, foodId) > 0;
		if (!affected) {
			assertActiveFood(foodId);
			return;
		}

		log.info("Food: soft deleted foodId={}", foodId);
	}

	private void assertActiveStore(Long storeId) {
		boolean exists = storeMapper.existsActive(storeId);
		if (!exists) {
			throw new StoreNotFoundException(storeId);
		}
	}

	private void assertActiveFood(Long foodId) {
		boolean exists = foodMapper.existsActive(foodId);
		if (!exists) {
			throw new FoodNotFoundException(foodId);
		}
	}
}
