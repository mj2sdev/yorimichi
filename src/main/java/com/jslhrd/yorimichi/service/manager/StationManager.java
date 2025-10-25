// src/main/java/com/jslhrd/yorimichi/service/manager/StationManager.java
package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.response.SliceResponse;
import com.jslhrd.yorimichi.domain.response.StationDTO;
import com.jslhrd.yorimichi.mapper.StationMapper;
import com.jslhrd.yorimichi.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 정류장(피드) 도메인의 Read 전용 매니저.
 * 리뷰/같이먹기 최신 데이터를 조회합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationManager implements StationService {

	private final StationMapper stationMapper;

	@Override
	public SliceResponse<StationDTO> findSlice(Long rootId, int size) {
		List<StationDTO> stations = stationMapper.selectSlice(rootId, size + 1);
		return SliceResponse.of(stations, size, StationDTO::getId);
	}
}
