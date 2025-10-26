// src/main/java/com/jslhrd/yorimichi/service/manager/StationManager.java
package com.jslhrd.yorimichi.service.manager;

import com.jslhrd.yorimichi.domain.CoeatDTO;
import com.jslhrd.yorimichi.domain.FoodDTO;
import com.jslhrd.yorimichi.domain.ReviewFoodDTO;
import com.jslhrd.yorimichi.domain.response.SliceResponse;
import com.jslhrd.yorimichi.domain.response.StationDTO;
import com.jslhrd.yorimichi.enums.RootType;
import com.jslhrd.yorimichi.mapper.CoeatRequestMapper;
import com.jslhrd.yorimichi.mapper.CommentMapper;
import com.jslhrd.yorimichi.mapper.ReviewFoodMapper;
import com.jslhrd.yorimichi.mapper.StationMapper;
import com.jslhrd.yorimichi.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 정류장(피드) 도메인의 Read 전용 매니저.
 * 리뷰/같이먹기 최신 데이터를 조회합니다.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationManager implements StationService {

	private final StationMapper stationMapper;
	private final ReviewFoodMapper reviewFoodMapper;
	private final CoeatRequestMapper coeatRequestMapper;
	private final CommentMapper commentMapper;

	private static FoodDTO toFoodDTO(ReviewFoodDTO reviewFood) {
		FoodDTO f = new FoodDTO();
		f.setId(reviewFood.getFoodId());
		f.setName(reviewFood.getFoodName());
		return f;
	}

	@Override
	public SliceResponse<StationDTO> findSlice(Long nextId, int size) {

		List<StationDTO> stations = stationMapper.selectSlice(nextId, size + 1);

		// 2) 배치용 id 수집
		List<Long> reviewIds = new ArrayList<>();
		List<Long> coeatIds = new ArrayList<>();

		for (StationDTO station : stations) {
			if (station.getType() == RootType.REVIEW) {
				reviewIds.add(station.getId());
			}
			if (station.getType() == RootType.COEAT) {
				coeatIds.add(station.getId());
			}
		}

		Map<Long, List<FoodDTO>> foodsByReviewId = Collections.emptyMap();
		if (!reviewIds.isEmpty()) {
			List<ReviewFoodDTO> rows = reviewFoodMapper.selectFoodNameByReviewIds(reviewIds);
			foodsByReviewId = rows.stream()
					.collect(Collectors.groupingBy(
							ReviewFoodDTO::getReviewId,
							Collectors.mapping(StationManager::toFoodDTO, Collectors.toList())
					));
		}

		Map<Long, CoeatDTO> countsByCoeatId = Collections.emptyMap();
		if (!coeatIds.isEmpty()) {
			countsByCoeatId = coeatRequestMapper.selectCoeatCountsByIds(coeatIds).stream()
					.filter(Objects::nonNull)
					.collect(Collectors.toMap(CoeatDTO::getId, c -> c, (a, b) -> a));
		}

		Map<Long, CoeatDTO> commentsByCoeatId = Collections.emptyMap();
		if (!coeatIds.isEmpty()) {
			commentsByCoeatId = commentMapper.selectCountsByIds(coeatIds).stream()
					.filter(Objects::nonNull)
					.collect(Collectors.toMap(CoeatDTO::getId, c -> c, (a, b) -> a));
		}

		for (StationDTO station : stations) {

			if (station.getType() == RootType.REVIEW) {
				List<FoodDTO> foods = foodsByReviewId.getOrDefault(station.getId(), List.of());
				station.getReview().setFoods(foods);
			}

			if (station.getType() == RootType.COEAT) {
				CoeatDTO cnt = countsByCoeatId.get(station.getId());
				int approved = cnt != null && cnt.getApprovedCount() != null ? cnt.getApprovedCount() : 0;
				int applied = cnt != null && cnt.getAppliedCount() != null ? cnt.getAppliedCount() : 0;
				station.getCoeat().setApprovedCount(approved);
				station.getCoeat().setAppliedCount(applied);

				CoeatDTO cc = commentsByCoeatId.get(station.getId());
				int comments = cc != null && cc.getCommentCount() != null ? cc.getCommentCount() : 0;
				station.getCoeat().setCommentCount(comments);
			}
		}

		return SliceResponse.of(stations, size, StationDTO::getId);
	}
}