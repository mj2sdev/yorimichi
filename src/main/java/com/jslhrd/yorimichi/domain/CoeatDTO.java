package com.jslhrd.yorimichi.domain;

import com.jslhrd.yorimichi.enums.CoeatStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

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
public class CoeatDTO extends RootDTO {

	/**
	 * FK: 유저 ID
	 */
	@NotNull
	@Positive
	private Long userId;

	/**
	 * FK: 상점 ID
	 */
	@Positive
	private Long storeId;

	/**
	 * 같이먹기 제목
	 */
	@NotBlank
	@Size(max = 100)
	private String title;

	/**
	 * 같이먹기 내용
	 */
	@NotBlank
	private String content;

	/**
	 * 같이먹기 정원
	 */
	@Min(1)
	private Integer capacity;

	/**
	 * 같이먹기 조회수
	 */
	@PositiveOrZero
	private Integer viewCount;

	/**
	 * 같이먹기 모임일시
	 */
	@NotNull
	@FutureOrPresent
	private LocalDateTime meetingAt;

	/**
	 * 같이먹기 상태
	 */
	@NotNull
	private CoeatStatus status;

	/**
	 * 자동 수락 여부
	 */
	@NotNull
	private Boolean autoAccept;


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
}