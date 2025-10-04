package com.jslhrd.yorimichi.service;

import com.jslhrd.yorimichi.domain.CoeatDTO;
import com.jslhrd.yorimichi.domain.CoeatRequestDTO;

import java.util.List;

/**
 * 같이먹기 서비스 인터페이스입니다.
 *
 * @author mj2sdev
 * @since 1.0
 */
public interface CoeatService {

	/**
	 * 같이먹기 리스트를 반환합니다.
	 *
	 * @return coeatDTO list
	 */
	public List<CoeatDTO> findAll();

	public List<CoeatDTO> findAllByStoreId(Long storeId);

	/**
	 * coeatId를 통해 같이먹기 상세정보를 반환합니다.
	 *
	 * @param coeatId 같이먹기 ID
	 * @return CoeatDTO
	 */
	public CoeatDTO findById(Long coeatId);

	/**
	 * 같이먹기 작성 데이터를 저장합니다.
	 *
	 * @param userId  유저 ID
	 * @param storeId 상점 ID
	 * @param dto     같이먹기 데이터
	 */
	public void save(Long userId, Long storeId, CoeatDTO dto);

	/**
	 * coeatDTO 를 통해 내용을 수정합니다.
	 *
	 * @param userId  유저 ID
	 * @param coeatId 같이먹기 ID
	 * @param dto     같이먹기 데이터
	 */
	public void update(Long userId, Long coeatId, CoeatDTO dto);

	/**
	 * coeatId 를 통해 같이먹기를 삭제합니다.
	 *
	 * @param coeatId 같이먹기 ID
	 */
	public void delete(Long userId, Long coeatId);

	/**
	 * 참여하고싶은 사람 (userId) 이 같이먹기 게시물(coeatId)에 추가됩니다.
	 *
	 * @param userId  유저 ID
	 * @param coeatId 같이먹기 ID
	 * @param dto
	 */
	public void saveCoeatRequest(Long userId, Long coeatId, CoeatRequestDTO dto);

	/**
	 * CoeatRequest에 있는 신청자가 Coeat리스트 안으로 들어가고, CoeatRequest에서는 삭제됩니다.
	 *
	 * @param userId       유저 ID
	 * @param coeatId      같이먹기 ID
	 * @param coeatRequest 같이먹기 신청 데이터
	 */
	public void updateCoeatRequestStatus(Long userId, Long coeatId, CoeatRequestDTO coeatRequest);

	/**
	 * CoeatRequest에 있는 신청자가 CoeatRequest에서는 삭제됩니다.
	 *
	 * @param userId  유저 ID
	 * @param coeatId 같이먹기 ID
	 */
	public void cancelCoeatRequest(Long userId, Long coeatId);
}
