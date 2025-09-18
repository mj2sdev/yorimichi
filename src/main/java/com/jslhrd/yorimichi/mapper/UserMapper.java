package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface UserMapper {
	int insert(UserDTO dto); // 회원 등록
	UserDTO selectById(@Param("id") Long id); // 단건 조회
	UserDTO selectByEmail(@Param("email") String email); // 이메일로 조회
	List<UserDTO> selectAll(); // 전체 회원 목록
	int update(UserDTO dto); // 회원 정보 수정
	int updateLastLoginAt(@Param("id") Long id); // 마지막 로그인 갱신
	int deleteById(@Param("id") Long id); // 삭제(PK)
}
