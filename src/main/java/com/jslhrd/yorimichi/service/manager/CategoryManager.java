package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.CategoryDTO;
import com.jslhrd.yorimichi.domain.SearchDTO;
import com.jslhrd.yorimichi.exception.*;
import com.jslhrd.yorimichi.mapper.CategoryMapper;
import com.jslhrd.yorimichi.mapper.StoreCategoryMapper;
import com.jslhrd.yorimichi.mapper.StoreMapper;
import com.jslhrd.yorimichi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		return List.of();
	}

	@Override
	public void save(CategoryDTO category) {

		try {
			boolean affected = categoryMapper.insert(category) > 0;
			if (!affected) {
				log.warn("Category insert failed: affected={}, category={}", affected, category);
				throw new IllegalStateException("Category insert failed");
			}
		} catch (DataIntegrityViolationException e) {

			throw new DuplicateCategoryException(category.getName());
		}

		log.info("Category created categoryId={}", category.getId());
	}

	@Override
	public void update(Long categoryId, CategoryDTO category) {

		boolean affected = categoryMapper.update(categoryId, category) > 0;
		if (affected) {
			log.info("Category updated categoryId={}", categoryId);
			return;
		}

		boolean exists = categoryMapper.existsById(categoryId);
		if (!exists) {
			throw new CategoryNotFoundException(categoryId);
		}
	}

	@Override
	public void delete(Long categoryId) {

		boolean affected = categoryMapper.deleteById(categoryId) > 0;
		if (affected) {
			log.info("Category deleted categoryId={}", categoryId);
			return;
		}

		boolean exists = categoryMapper.existsById(categoryId);
		if (!exists) {
			throw new CategoryNotFoundException(categoryId);
		}
	}

	@Override
	public void addCategoryToStore(Long storeId, Long categoryId) {

		boolean storeExists = storeMapper.existsActive(storeId);
		if (!storeExists) {
			throw new StoreNotFoundException(storeId);
		}

		boolean categoryExists = categoryMapper.existsById(categoryId);
		if (!categoryExists) {
			throw new CategoryNotFoundException(categoryId);
		}

		try {
			boolean affected = storeCategoryMapper.insert(storeId, categoryId) > 0;
			if (affected) {
				log.warn("StoreCategory liked failed: affected={}, storeId={}, categoryId={}", affected, storeId, categoryId);
				throw new IllegalStateException("StoreCategory liked failed");
			}
		} catch (DataIntegrityViolationException e) {
			throw new DuplicateStoreCategoryException(storeId, categoryId);
		}

		log.info("StoreCategory liked storeId={}, categoryId={}", storeId, categoryId);
	}


	@Override
	public void removeCategoryFromStore(Long storeId, Long categoryId) {

		boolean affected = storeCategoryMapper.delete(storeId, categoryId) > 0;
		if (affected) {
			log.info("StoreCategory unliked storeId={}, categoryId={}", storeId, categoryId);
			return;
		}

		boolean exists = storeCategoryMapper.exists(storeId, categoryId);
		if (!exists) {
			throw new StoreCategoryNotFoundException(storeId, categoryId);
		}
	}
}