package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.FeedCoeatRequestDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;


@Mapper
public interface FeedCoeatRequestMapper {
    int insert(FeedCoeatRequestDTO req);    // (feedId, userId, status, message, createdAt)
    int delete(FeedCoeatRequestDTO req);    // (feedId, userId) 기준 취소
    int count(FeedCoeatRequestDTO req);     // 존재 여부(0/1)
    List<FeedCoeatRequestDTO> listByFeedId(Long feedId);
    List<FeedCoeatRequestDTO> listByUserId(Long userId);
    int updateStatus(FeedCoeatRequestDTO req); // status 변경(승인/거절 등)
}
