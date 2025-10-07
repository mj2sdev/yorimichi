package com.jslhrd.yorimichi.service;

import com.jslhrd.yorimichi.domain.FoodDTO;

import java.util.List;

public interface FoodService {

	public List<FoodDTO> findAll();

	public void save(FoodDTO food);

	public void update(Long storeId, Long foodId, FoodDTO food);

	public void delete(Long storeId, Long foodId);
}