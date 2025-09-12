package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.FeedCoeatDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface FeedCoeatMapper {
    int insert(FeedCoeatDTO coeat);
    FeedCoeatDTO selectById(Long feedCoeatId);
    List<FeedCoeatDTO> listByFeedId(Long feedId);
    List<FeedCoeatDTO> listByStoreId(Long storeId);
    int update(FeedCoeatDTO coeat);         
    int deleteById(Long feedCoeatId);
}
