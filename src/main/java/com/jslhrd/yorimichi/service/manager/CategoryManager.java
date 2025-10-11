package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.CategoryDTO;
import com.jslhrd.yorimichi.domain.SearchDTO;
import com.jslhrd.yorimichi.exception.BadRequestException;
import com.jslhrd.yorimichi.exception.CategoryNotFoundException;
import com.jslhrd.yorimichi.exception.DuplicateCategoryException;
import com.jslhrd.yorimichi.exception.StoreNotFoundException;
import com.jslhrd.yorimichi.mapper.CategoryMapper;
import com.jslhrd.yorimichi.mapper.StoreCategoryMapper;
import com.jslhrd.yorimichi.mapper.StoreMapper;
import com.jslhrd.yorimichi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryManager implements CategoryService {

	private final CategoryMapper categoryMapper;
	private final StoreCategoryMapper storeCategoryMapper;
	private final StoreMapper storeMapper;

	@Override
	public List<CategoryDTO> findAll() {
		return categoryMapper.selectAll();
	}

	@Override
	public List<CategoryDTO> findAllByDTO(SearchDTO category) {
		// TODO: 어떤 역할인가요?
		return Collections.emptyList();
	}

	@Override
	public void save(CategoryDTO category) {

		assertExistParent(category.getParentId());

		try {
			categoryMapper.insert(category);
		} catch (DuplicateKeyException e) {
			throw new DuplicateCategoryException(category.getName());
		}

		log.info("Category: created categoryId={}", category.getId());
	}

	@Override
	public void update(Long categoryId, CategoryDTO category) {

		if (category.getParentId() != null && categoryId.equals(category.getParentId())) {
			throw new BadRequestException("자기 자신을 부모로 지정할 수 없습니다.");
		}

		if (category.getId() != null && !categoryId.equals(category.getId())) {
			throw new BadRequestException("경로의 categoryId 와 본문의 id 가 다릅니다.");
		}

		assertExistParent(category.getParentId());

		try {
			boolean affected = categoryMapper.update(categoryId, category) > 0;
			if (!affected) {
				assertExistCategory(categoryId);
				log.debug("Category: update no-op categoryId={}, category={}", categoryId, category);
				return;
			}
		} catch (DuplicateKeyException e) {
			throw new DuplicateCategoryException(category.getName());
		}

		log.info("Category: updated categoryId={}", categoryId);
	}

	@Override
	public void delete(Long categoryId) {

		boolean affected = categoryMapper.deleteById(categoryId) > 0;
		if (!affected) {
			assertExistCategory(categoryId);
			log.debug("Category: delete no-op categoryId={}", categoryId);
			return;
		}

		log.info("Category: deleted categoryId={}", categoryId);
	}

	@Override
	public void addCategoryToStore(Long storeId, Long categoryId) {

		assertActiveStore(storeId);
		assertExistCategory(categoryId);

		try {
			storeCategoryMapper.insert(storeId, categoryId);
		} catch (DuplicateKeyException e) {
			log.info("StoreCategory: add no-op storeId={}, categoryId={}", storeId, categoryId);
			return;
		}

		log.info("StoreCategory: add storeId={}, categoryId={}", storeId, categoryId);
	}


	@Override
	public void removeCategoryFromStore(Long storeId, Long categoryId) {

		boolean affected = storeCategoryMapper.delete(storeId, categoryId) > 0;
		if (!affected) {
			log.debug("StoreCategory: remove no-op storeId={}, categoryId={}", storeId, categoryId);
			return;
		}

		log.info("StoreCategory: removed storeId={}, categoryId={}", storeId, categoryId);
	}

	private void assertExistCategory(Long categoryId) {
		boolean exists = categoryMapper.existsById(categoryId);
		if (!exists) {
			throw new CategoryNotFoundException(categoryId);
		}
	}

	private void assertExistParent(Long parentId) {
		if (parentId == null) {
			return;
		}
		assertExistCategory(parentId);
	}

	private void assertActiveStore(Long storeId) {
		boolean exists = storeMapper.existsActive(storeId);
		if (!exists) {
			throw new StoreNotFoundException(storeId);
		}
	}
}