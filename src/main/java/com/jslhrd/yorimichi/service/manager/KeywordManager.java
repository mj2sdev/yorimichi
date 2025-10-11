package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.KeywordDTO;
import com.jslhrd.yorimichi.exception.BadRequestException;
import com.jslhrd.yorimichi.exception.DuplicateKeywordException;
import com.jslhrd.yorimichi.exception.KeywordNotFoundException;
import com.jslhrd.yorimichi.exception.RootNotFoundException;
import com.jslhrd.yorimichi.mapper.KeywordMapper;
import com.jslhrd.yorimichi.mapper.RootKeywordMapper;
import com.jslhrd.yorimichi.mapper.RootMapper;
import com.jslhrd.yorimichi.service.KeywordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class KeywordManager implements KeywordService {

	private final KeywordMapper keywordMapper;
	private final RootMapper rootMapper;
	private final RootKeywordMapper rootKeywordMapper;

	@Override
	@Transactional(readOnly = true)
	public List<KeywordDTO> findAll() {
		return keywordMapper.selectAll();
	}

	@Override
	public void save(KeywordDTO keyword) {

		try {
			keywordMapper.insert(keyword);
		} catch (DuplicateKeyException e) {
			throw new DuplicateKeywordException(keyword.getName());
		}

		log.info("Keyword: created keywordId={}", keyword.getId());
	}

	@Override
	public void update(Long keywordId, KeywordDTO keyword) {

		if (keyword.getId() != null && !keywordId.equals(keyword.getId())) {
			throw new BadRequestException("경로의 keywordId 와 본문의 id 가 다릅니다.");
		}

		try {
			boolean affected = keywordMapper.update(keywordId, keyword) > 0;
			if (!affected) {
				assertExistKeyword(keywordId);
				log.debug("Keyword: update no-op keywordId={}, keyword={}", keywordId, keyword);
				return;
			}
		} catch (DuplicateKeyException e) {
			throw new DuplicateKeywordException(keyword.getName());
		}

		log.info("Keyword: updated keywordId={}", keywordId);
	}

	@Override
	public void delete(Long keywordId) {

		boolean affected = keywordMapper.deleteById(keywordId) > 0;
		if (!affected) {
			assertExistKeyword(keywordId);
			log.debug("Keyword: delete no-op keywordId={}", keywordId);
			return;
		}

		log.info("Keyword: deleted keywordId={}", keywordId);
	}

	@Override
	public void addKeywordToRoot(Long rootId, Long keywordId) {

		assertActiveRoot(rootId);
		assertExistKeyword(keywordId);

		try {
			rootKeywordMapper.insert(rootId, keywordId);
		} catch (DuplicateKeyException e) {
			log.debug("RootKeyword: add no-op rootId={}, keywordId={}", rootId, keywordId);
			return;
		}

		log.info("RootKeyword: add rootId={}, keywordId={}", rootId, keywordId);
	}


	@Override
	public void removeKeywordFromRoot(Long rootId, Long keywordId) {

		boolean affected = rootKeywordMapper.delete(rootId, keywordId) > 0;
		if (!affected) {
			log.debug("RootKeyword: remove no-op rootId={}, keywordId={}", rootId, keywordId);
			return;
		}

		log.info("RootKeyword: removed rootId={}, keywordId={}", rootId, keywordId);
	}

	private void assertExistKeyword(Long keywordId) {
		boolean exists = keywordMapper.existsById(keywordId);
		if (!exists) {
			throw new KeywordNotFoundException(keywordId);
		}
	}

	private void assertActiveRoot(Long rootId) {
		boolean exists = rootMapper.existsActive(rootId);
		if (!exists) {
			throw new RootNotFoundException(rootId);
		}
	}
}