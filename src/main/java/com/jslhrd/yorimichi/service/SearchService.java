// com.jslhrd.yorimichi.service.SearchService
package com.jslhrd.yorimichi.service;

import java.util.List;
import com.jslhrd.yorimichi.domain.SearchResultDTO;
import com.jslhrd.yorimichi.domain.SuggestDTO;

public interface SearchService {
    List<SuggestDTO> suggest(String q, int limit);
        // 검색 페이지용 (간단 버전)
    SearchResultDTO search(String q, String type, Long id, int page, int size);
}
