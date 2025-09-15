package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.RootKeywordDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;


@Mapper
public interface RootKeywordMapper {
    int insert(RootKeywordDTO rootKeyword);  // (rootId, keywordId) + createdAt=NOW()
    int delete(RootKeywordDTO rootKeyword);
    int count(RootKeywordDTO rootKeyword);
    List<RootKeywordDTO> listByRootId(Long rootId);
    List<RootKeywordDTO> listByKeywordId(Long keywordId);
}
