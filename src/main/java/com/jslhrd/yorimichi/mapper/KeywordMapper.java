package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.KeywordDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;


@Mapper
public interface KeywordMapper {
    int insert(KeywordDTO keyword);
    KeywordDTO selectById(Long keywordId);
    KeywordDTO selectByName(String name);
    List<KeywordDTO> listByNameLike(String keyword); // 부분검색
    int update(KeywordDTO keyword);                  // 이름 변경 등
    int deleteById(Long keywordId);
}
