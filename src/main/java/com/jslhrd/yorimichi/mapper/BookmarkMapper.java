package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.BookmarkDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BookmarkMapper {

    int insert(BookmarkDTO dto); // 북마크 등록

    // 복합키 삭제 — 키 파라미터 명시
    int delete(@Param("userId") Long userId,
               @Param("storeId") Long storeId);

    int count(@Param("userId") Long userId,
              @Param("storeId") Long storeId); // 중복 여부 확인(0/1)

    List<BookmarkDTO> listByUserId(@Param("userId") Long userId);   // 유저의 북마크 목록
    List<BookmarkDTO> listByStoreId(@Param("storeId") Long storeId); // 상점을 북마크한 목록
}
