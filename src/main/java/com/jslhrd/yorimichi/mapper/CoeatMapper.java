package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.CoeatDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 같이먹기 Mapper.
 *
 * <br>같이먹기 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface CoeatMapper {

	/**
	 * 같이먹기 목록 조회.
	 *
	 * @return DTO를 담은 List
	 */
	List<CoeatDTO> selectAll();

	List<CoeatDTO> selectAllByStoreId(@Param("storeId") Long storeId);

	/**
	 * 같이먹기 단건 조회.
	 *
	 * @param coeatId 같이먹기 ID
	 * @return 존재하면 DTO를 담은 Optional, 없으면 Optional.empty()
	 */
	Optional<CoeatDTO> selectById(@Param("coeatId") Long coeatId);

	int selectCapacityById(@Param("coeatId") Long coeatId);

	boolean existsActive(@Param("coeatId") Long coeatId);

	boolean isOwner(@Param("userId") Long userId,
	                @Param("coeatId") Long coeatId);

	boolean isOpen(@Param("coeatId") Long coeatId);

	/**
	 * 같이먹기 추가.
	 *
	 */
	void insert(CoeatDTO coeat);

	/**
	 * 같이먹기 수정.
	 *
	 * @param userId  유저 ID
	 * @param coeatId 같이먹기 coeatId
	 * @param coeat   수정된 DTO
	 * @return 영향 행 수 (수정 1, 대상 없음 0)
	 */
	int update(@Param("userId") Long userId,
	           @Param("coeatId") Long coeatId,
	           @Param("coeat") CoeatDTO coeat);

	/**
	 * 같이먹기 삭제.
	 *
	 * @param userId  유저 ID
	 * @param coeatId 같이먹기 ID
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int deleteById(@Param("userId") Long userId,
	               @Param("coeatId") Long coeatId);
}