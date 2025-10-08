package com.jslhrd.yorimichi.service;

import com.jslhrd.yorimichi.domain.StoreDTO;

import java.util.List;

public interface BookmarkService {

	public List<StoreDTO> findBookmarks(Long userId);

	public void save(Long userId, Long storeId);

	public void delete(Long userId, Long storeId);
}