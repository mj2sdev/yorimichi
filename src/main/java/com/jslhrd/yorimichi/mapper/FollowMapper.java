package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.FollowDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface FollowMapper {
    int insert(FollowDTO follow); // 팔로우 등록
    int delete(FollowDTO follow); // 언팔로우
    int count(FollowDTO follow); // 팔로우 관계 확인(0 or 1)
    List<FollowDTO> listFollowersOf(Long userId); // 나를 팔로우하는 사람 목록(= 내 팔로워들)
    List<FollowDTO> listFolloweesOf(Long userId); // 내가 팔로우하는 사람 목록(= 내 팔로이들)
    int updateNotified(FollowDTO follow); // 알림 여부 갱신
}
