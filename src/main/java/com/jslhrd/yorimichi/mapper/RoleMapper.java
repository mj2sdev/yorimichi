package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.RoleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface RoleMapper {
	int insert(RoleDTO dto); // 역할 등록
	RoleDTO selectById(@Param("id") Long id); // 단건 조회
	List<RoleDTO> selectAll(); // 전체 목록
	int update(RoleDTO dto); // 이름/권한 수정
	int deleteById(@Param("id") Long id); // 삭제(PK)
}
