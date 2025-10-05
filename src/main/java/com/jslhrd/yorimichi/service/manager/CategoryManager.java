package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.CategoryDTO;
import com.jslhrd.yorimichi.domain.SearchDTO;
import com.jslhrd.yorimichi.domain.StoreCategoryDTO;
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

	@Transactional(readOnly = true)
	@Override
	public List<CategoryDTO> findAll(SearchDTO q) {
		return categoryMapper.selectAll(q);
	}

	@Override
	public void save(CategoryDTO dto) {

		try {
			int affected = categoryMapper.insert(dto);
			if (affected == 0) {
				log.warn("Category insert failed: affected={}, dto={}", affected, dto);
				throw new IllegalStateException("Category insert failed");
			}
		} catch (DataIntegrityViolationException e) {

			throw new DuplicateCategoryException(dto.getName());
		}

		log.info("Category created id={}", dto.getId());
	}

	@Override
	public void update(Long categoryId, CategoryDTO dto) {

		int affected = categoryMapper.update(categoryId, dto);
		if (affected == 1) {
			log.info("Category updated id={}", categoryId);
			return;
		}

		boolean exists = categoryMapper.existsById(categoryId);
		if (!exists) {
			throw new CategoryNotFoundException(categoryId);
		}
	}

	@Override
	public void delete(Long categoryId) {

		int affected = categoryMapper.deleteById(categoryId);
		if (affected == 1) {
			log.info("Category deleted id={}", categoryId);
			return;
		}

		boolean exists = categoryMapper.existsById(categoryId);
		if (!exists) {
			throw new CategoryNotFoundException(categoryId);
		}
	}

	@Override
	public void linkCategoryToStore(Long storeId, Long categoryId) {

		boolean storeExists = storeMapper.existsActive(storeId);
		if (!storeExists) {
			throw new StoreNotFoundException(storeId);
		}

		boolean categoryExists = categoryMapper.existsById(categoryId);
		if (!categoryExists) {
			throw new CategoryNotFoundException(categoryId);
		}

		StoreCategoryDTO dto = new StoreCategoryDTO(storeId, categoryId);
		try {
			int affected = storeCategoryMapper.insert(dto);
			if (affected == 0) {
				log.warn("StoreCategory liked failed: affected={}, dto={}", affected, dto);
				throw new IllegalStateException("StoreCategory liked failed");
			}
		} catch (DataIntegrityViolationException e) {
			throw new DuplicateStoreCategoryException(storeId, categoryId);
		}

		log.info("StoreCategory liked storeId={} and categoryId={}", storeId, categoryId);
	}


	@Override
	public void unlinkCategoryFromStore(Long storeId, Long categoryId) {

		StoreCategoryDTO dto = new StoreCategoryDTO(storeId, categoryId);
		int affected = storeCategoryMapper.delete(dto);
		if (affected == 1) {
			log.info("StoreCategory unliked storeId={} and categoryId={}", storeId, categoryId);
			return;
		}

		boolean exists = storeCategoryMapper.exists(dto);
		if (!exists) {
			throw new StoreCategoryNotFoundException(storeId, categoryId);
		}
	}
}