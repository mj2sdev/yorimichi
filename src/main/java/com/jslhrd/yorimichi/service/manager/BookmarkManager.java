package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.StoreDTO;
import com.jslhrd.yorimichi.exception.StoreNotFoundException;
import com.jslhrd.yorimichi.exception.UserNotFoundException;
import com.jslhrd.yorimichi.mapper.BookmarkMapper;
import com.jslhrd.yorimichi.mapper.StoreMapper;
import com.jslhrd.yorimichi.mapper.UserMapper;
import com.jslhrd.yorimichi.service.BookmarkService;
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
public class BookmarkManager implements BookmarkService {

	private final StoreMapper storeMapper;
	private final UserMapper userMapper;
	private final BookmarkMapper bookmarkMapper;

	@Override
	@Transactional(readOnly = true)
	public List<StoreDTO> findBookmarks(Long userId) {
		return bookmarkMapper.selectByUserId(userId);
	}

	@Override
	public void save(Long userId, Long storeId) {

		assertActiveUser(userId);
		assertActiveStore(storeId);

		try {
			bookmarkMapper.insert(userId, storeId);
		} catch (DuplicateKeyException e) {
			log.debug("Bookmark: no-op userId={}, storeId={}", userId, storeId);
			return;
		}

		log.info("Bookmark: created userId={}, storeId={}", userId, storeId);
	}

	@Override
	public void delete(Long userId, Long storeId) {

		boolean affected = bookmarkMapper.delete(userId, storeId) > 0;
		if (!affected) {
			log.debug("Bookmark: delete no-op userId={}, storeId={}", userId, storeId);
			return;
		}

		log.info("Bookmark: deleted userId={}, storeId={}", userId, storeId);
	}

	private void assertActiveUser(Long userId) {
		boolean exist = userMapper.existsActive(userId);
		if (!exist) {
			throw new UserNotFoundException(userId);
		}
	}

	private void assertActiveStore(Long storeId) {
		boolean exist = storeMapper.existsActive(storeId);
		if (!exist) {
			throw new StoreNotFoundException(storeId);
		}
	}
}