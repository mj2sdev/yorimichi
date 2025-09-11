package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.PostalDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface PostalMapper {
    int insert(PostalDTO postal); // 등록
    PostalDTO selectById(Long postalId); // 조회
    PostalDTO selectByCode(String code); // 코드로 조회
    List<PostalDTO> listByRoadId(Long roadId); // 특정 도로의 우편번호 목록
    int update(PostalDTO postal); // 수정
    int deleteById(Long postalId); // 삭제
}
