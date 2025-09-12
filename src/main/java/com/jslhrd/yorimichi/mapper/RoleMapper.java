package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.RoleDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface RoleMapper {
    int insert(RoleDTO role);
    RoleDTO selectById(Long roleId);
    RoleDTO selectByName(String name);
    List<RoleDTO> listAll();
    int update(RoleDTO role);
    int deleteById(Long roleId);
}
