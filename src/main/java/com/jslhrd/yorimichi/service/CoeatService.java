package com.jslhrd.yorimichi.service;

import java.security.Principal;
import java.util.List;

import com.jslhrd.yorimichi.domain.CoeatDTO;
import com.jslhrd.yorimichi.domain.CoeatRequestDTO;

/**
 * 같이먹기 서비스 인터페이스입니다.
 * 
 * @author mj2sdev
 * @since 1.0
 * TODO: FeedCoeatDTO가 바뀔 예정이므로 Object로 임시 변경
 */
public interface CoeatService {
	
	/**
	 * 같이먹기 글 내용 저장
	 * 
	 * @param dto
	 */
	public void save(Object dto);

	/**
	 * feedDTO 를 통해 내용을 수정합니다.
	 * @param coeatId 대상이 되는 id
	 * @param dto 수정할 내용을 담은 DTO
	 */
	public void updateCoeat(Long coeatId, Object dto);
	
	/**
	 * feedId 를 통해 같이먹기 feed를 삭제합니다.
	 * @param coeatId
	 */
	public void deleteCoeat(Long coeatId);

	/**
	 * 참여하고싶은 사람 (userId) 이 같이먹기 게시물(coeatId)에 추가됩니다.
	 * @param userId
	 * @param requestDTO 같이먹기 요청 자기소개를 담은 DTO
	 * @param coeatId
	 */
	public void joinCoeat(Long coeatId, CoeatRequestDTO requestDTO, Long userId);

	/**
	 * CoeatRequest에 있는 신청자가 Coeat리스트 안으로 들어가고, CoeatRequest에서는 삭제됩니다.
	 * TODO: 컨트롤러를 작성하며 임시로 작성해둔 서비스 검토 필요
	 * @param coeatId 같이먹기의 Id
	 * @param participantId 신청자가 누구인지
	 * @param userId 글 작성자가 맞는지 체크용
	 */
	public void acceptParticipant(Long coeatId, Long participantId, Long userId);

	/**
	 * CoeatRequest에 있는 신청자가 CoeatRequest에서는 삭제됩니다.
	 * TODO: 컨트롤러를 작성하며 임시로 작성해둔 서비스 검토 필요
	 * principal은 작성자가 맞는지 확인용
	 * @param coeatId 같이먹기의 Id
	 * @param participantId 신청자가 누구인지
	 * @param userId 글 작성자가 맞는지 체크용
	 */
	public void rejectParticipant(Long coeatId, Long participantId, Long userId);

	/**
	 * coeatId를 통해 같이먹기 상세정보를 반환합니다.
	 * TODO: 컨트롤러를 작성하며 임시로 작성해둔 서비스 검토 필요
	 * @param coeatId
	 * @return CoeatDTO
	 */
	public CoeatDTO getCoeatDetail(Long coeatId);

	/**
	 * 같이먹기 리스트를 반환합니다.
	 * TODO: 컨트롤러를 작성하며 임시로 작성해둔 서비스 검토 필요
	 * @return List<CoeatDTO>
	 */
	public List<CoeatDTO> getCoeatList();

	/**
	 * 특정 가게의 같이먹기 리스트를 반환합니다.
	 * @param storeId
	 * @return 
	 */
	public List<CoeatDTO> getCoeatListById(Long storeId);
}
