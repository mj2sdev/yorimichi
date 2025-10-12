package com.jslhrd.yorimichi.service;

import com.jslhrd.yorimichi.domain.KeywordDTO;

import java.util.List;

public interface KeywordService {

	public List<KeywordDTO> findAll();

	public void save(KeywordDTO Keyword);

	public void update(Long keywordId, KeywordDTO Keyword);

	public void delete(Long KeywordId);

	public void addKeywordToRoot(Long rootId, Long KeywordId);

	public void removeKeywordFromRoot(Long rootId, Long KeywordId);
}