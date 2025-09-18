package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.PostalDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PostalMapper {
	int insert(PostalDTO dto); // 우편번호 등록
	PostalDTO selectById(@Param("id") Long id); // 단건 조회
	List<PostalDTO> selectByCode(@Param("code") String code); // 우편번호 검색
	List<PostalDTO> selectByRegionEmdId(@Param("emdId") Long emdId); // 행정동 기준 목록
	int update(PostalDTO dto); // 주소 정보 수정
	int deleteById(@Param("id") Long id); // 삭제(PK)
	int deleteByRegionEmdId(@Param("emdId") Long emdId); // 행정동 기준 일괄 삭제
}
