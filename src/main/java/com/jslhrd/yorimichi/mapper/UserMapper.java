package com.jslhrd.yorimichi.mapper;
import com.jslhrd.yorimichi.domain.UserDTO;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper {
    int insert(UserDTO user); // 사용자 신규 등록 (성공 시 1 반환)
    UserDTO selectById(Long userId); // PK로 사용자 조회 (없으면 Null)
    UserDTO selectByEmail(String email); // email로 사용자 조회
    int update(UserDTO user); // 사용자 정보 수정
    int deleteById(Long userId); // 사용자 삭제(성공 시 1)

}
