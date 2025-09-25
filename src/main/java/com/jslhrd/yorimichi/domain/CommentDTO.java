package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

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
@Alias("CommentDTO")
public class CommentDTO extends RootDTO {

	/**
	 * FK: 같이먹기 ID
	 */
	private Long coeatId;

	/**
	 * FK: 부모 댓글 ID
	 */
	private Long parentId;

	/**
	 * FK: 유저 ID
	 */
	private Long userId;

	/**
	 * 댓글 내용
	 */
	private String content;
}