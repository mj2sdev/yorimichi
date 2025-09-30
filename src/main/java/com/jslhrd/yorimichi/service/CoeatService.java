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
	 * @param feedDTO
	 */
	public void updateCoeat(Object dto);
	
	/**
	 * feedId 를 통해 같이먹기 feed를 삭제합니다.
	 * @param FeedId
	 */
	public void deleteCoeat(Long feedId);

	/**
	 * 참여하고싶은 사람 (userId) 이 같이먹기 게시물(feedId)에 추가됩니다.
	 * @param userId
	 * @param FeedId
	 */
	public void joinCoeat(Long userId, Long feedId);

	/**
	 * CoeatRequest에 있는 신청자가 Coeat리스트 안으로 들어가고, CoeatRequest에서는 삭제됩니다.
	 * TODO: 컨트롤러를 작성하며 임시로 작성해둔 서비스 검토 필요
	 * principal은 작성자가 맞는지 확인용
	 * @param principal
	 * @param coeatRequest
	 */
	public void acceptParticipant(Principal principal, CoeatRequestDTO coeatRequest);

	/**
	 * CoeatRequest에 있는 신청자가 CoeatRequest에서는 삭제됩니다.
	 * TODO: 컨트롤러를 작성하며 임시로 작성해둔 서비스 검토 필요
	 * principal은 작성자가 맞는지 확인용
	 * @param principal
	 * @param coeatRequest
	 */
	public void rejectParticipant(Principal principal, CoeatRequestDTO coeatRequest);

	/**
	 * feedId를 통해 같이먹기 상세정보를 반환합니다.
	 * TODO: 컨트롤러를 작성하며 임시로 작성해둔 서비스 검토 필요
	 * @param feedId
	 * @return CoeatDTO
	 */
	public CoeatDTO getCoeatDetail(Long feedId);

	/**
	 * 같이먹기 리스트를 반환합니다.
	 * TODO: 컨트롤러를 작성하며 임시로 작성해둔 서비스 검토 필요
	 * @return List<CoeatDTO>
	 */
	public List<CoeatDTO> getCoeatList();
}
