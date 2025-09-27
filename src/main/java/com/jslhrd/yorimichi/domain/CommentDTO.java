package com.jslhrd.yorimichi.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 댓글 DTO.
 * <p>
 * 댓글 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
public class CommentDTO extends RootDTO {

	/**
	 * FK: 같이먹기 ID
	 */
	@NotNull
	@Positive
	private Long coeatId;

	/**
	 * FK: 부모 댓글 ID
	 */
	@Positive
	private Long parentId;

	/**
	 * FK: 유저 ID
	 */
	@NotNull
	@Positive
	private Long userId;

	/**
	 * 댓글 내용
	 */
	@NotBlank
	private String content;


	/**
	 * 작성된 같이먹기
	 */
	private CoeatDTO coeat;

	/**
	 * 작성한 유저
	 */
	private UserDTO user;
	;

	/**
	 * 댓글 목록
	 */
	private List<CommentDTO> comments;
}