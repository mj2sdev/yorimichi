package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.RootImageDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;


@Mapper
public interface RootImageMapper {
    int insert(RootImageDTO rootImage);      // (rootId, imageId) + createdAt=NOW()
    int delete(RootImageDTO rootImage);
    int count(RootImageDTO rootImage);       // 존재 여부(0/1)
    List<RootImageDTO> listByRootId(Long rootId);
    List<RootImageDTO> listByImageId(Long imageId);
}
