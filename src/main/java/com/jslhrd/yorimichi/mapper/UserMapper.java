package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    int insert(UserDTO user);

    UserDTO selectById(@Param("userId") Long userId);      
    UserDTO selectByEmail(@Param("email") String email);   
    int update(UserDTO user);
    int deleteById(@Param("userId") Long userId);          
}
