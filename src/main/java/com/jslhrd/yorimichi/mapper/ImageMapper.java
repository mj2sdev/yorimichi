package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.ImageDTO;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ImageMapper {
    int insert(ImageDTO image);
    ImageDTO selectById(Long imageId);
    int update(ImageDTO image);   // url 등 수정
    int deleteById(Long imageId);
}
