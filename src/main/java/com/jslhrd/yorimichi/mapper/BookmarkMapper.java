package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.BookmarkDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface BookmarkMapper {
    int insert(BookmarkDTO bookmark); // 북마크 추가
    int delete(BookmarkDTO bookmark); // 북마크 삭제
    int count(BookmarkDTO bookmark); // 북마크 존재 여부(0 / 1)
    List<BookmarkDTO> listByUserId(Long userId); // 특정 사용자의 북마크 목록
}
