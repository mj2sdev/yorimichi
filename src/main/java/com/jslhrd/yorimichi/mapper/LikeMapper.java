package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.LikeDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface LikeMapper {
    int insert(LikeDTO like); // 좋아요 추가
    int delete(LikeDTO like); // 좋아요 취소
    int count(LikeDTO like); // 좋아요 존재 여부
    List<LikeDTO> listByUserId(Long UserId); // 특정 사용자가 누른 좋아요 목록
    List<LikeDTO> listByRootId(Long RootId); // 특정 루트에 대한 좋아요 목록
}
