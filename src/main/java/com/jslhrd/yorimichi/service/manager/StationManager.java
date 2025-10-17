// src/main/java/com/jslhrd/yorimichi/service/manager/StationManager.java
package com.jslhrd.yorimichi.service.manager;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jslhrd.yorimichi.domain.CoeatDTO;
import com.jslhrd.yorimichi.domain.ReviewDTO;
import com.jslhrd.yorimichi.mapper.StationMapper;
import com.jslhrd.yorimichi.service.StationService;

import lombok.RequiredArgsConstructor;

/**
 * 정류장(피드) 도메인의 Read 전용 매니저.
 * 리뷰/같이먹기 최신 데이터를 조회합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationManager implements StationService {

    private final StationMapper stationMapper;

    /** 최신 리뷰 n건 */
    @Override
    public List<ReviewDTO> findLatestReviews(int count) {
        return stationMapper.selectLatestReviews(count);
    }

    /** 최신 같이먹기 n건 */
    @Override
    public List<CoeatDTO> findLatestCoeats(int count) {
        return stationMapper.selectLatestCoeats(count);
    }

    // 필요하면 혼합 피드까지 제공할 수도 있어요.
    // 컨트롤러에서 merge 하기로 했으면 아래는 생략해도 됩니다.
    // public List<Map<String, Object>> findLatestMixed(int count) { ... }
}
