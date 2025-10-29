package com.jslhrd.yorimichi.domain;

import com.jslhrd.yorimichi.enums.CoeatStatus;
import com.jslhrd.yorimichi.enums.RootType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 같이먹기 DTO.
 * <p>
 * 같이먹기 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
@ToString
public class CoeatDTO extends RootDTO implements FeedDTO {

	/**
	 * FK: 유저 ID
	 */
	private Long userId;
	/**
	 * FK: 상점 ID
	 */
	private Long storeId;
	/**
	 * 같이먹기 제목
	 */
	private String title;
	/**
	 * 같이먹기 내용
	 */
	private String content;
	/**
	 * 같이먹기 정원
	 */
	private Integer capacity;

	private Integer approvedCount;
	private Integer appliedCount;

	/**
	 * 같이먹기 조회수
	 */
	private Integer viewCount;
	/**
	 * 같이먹기 모임일시
	 */
	private LocalDateTime meetingAt;
	/**
	 * 같이먹기 상태
	 */
	private CoeatStatus status;
	/**
	 * 자동 수락 여부
	 */
	private Boolean autoAccept;

	private Integer commentCount;


	/**
	 * 같이먹기 상점
	 */
	private StoreDTO store;
	/**
	 * 같이먹기 작성자
	 */
	private UserDTO user;
	/**
	 * 같이먹기 신청자 목록
	 */
	private List<CoeatRequestDTO> coeatRequests;
	/**
	 * 같이먹기 댓글 목록
	 */
	private List<CommentDTO> comments;

	private Integer count;

	public CoeatDTO() {
		super(RootType.COEAT);
	}
}