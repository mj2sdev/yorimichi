package com.jslhrd.yorimichi.mapper;
import com.jslhrd.yorimichi.domain.FeedDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FeedMapper {
    
    int insert(FeedDTO feed); // 피드 생성 (성공 시 1)
    FeedDTO selectByID(Long feedId); //피드 조회(없으면 Null)
    int update(FeedDTO feed); // 피드 내용/타입 업데이트
    int increaseViewCount(Long feedId); // 조회수 
    int deleteById(Long feedId); // 피드 삭제

}
