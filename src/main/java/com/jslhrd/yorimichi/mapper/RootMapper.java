package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.RootDTO;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface RootMapper {
    int insert(RootDTO root);         
    RootDTO selectById(Long rootId);
    int update(RootDTO root);
    int deleteById(Long rootId);          
}
