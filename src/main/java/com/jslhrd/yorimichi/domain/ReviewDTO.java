package com.jslhrd.yorimichi.domain;

import com.jslhrd.yorimichi.enums.RootType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 리뷰 DTO.
 *
 * <br>리뷰 정보를 전닿합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
public class ReviewDTO extends RootDTO implements FeedDTO {

	/**
	 * FK: 유저 ID
	 */
	private Long userId;

	/**
	 * FK: 상점 ID
	 */
	private Long storeId;

	/**
	 * 리뷰 평점
	 */
	private Integer rating;

	/**
	 * 리뷰 내용
	 */
	private String content;


	/**
	 * 리뷰 작성한 유저
	 */
	private UserDTO user;

	/**
	 * 리뷰 작성된 상점
	 */
	private StoreDTO store;

	/**
	 * 리뷰 음식 목록
	 */
	private List<FoodDTO> foods;

	/**
	 * 리뷰 이미지 목록
	 */
	private List<ImageDTO> images;

	/**
	 * front에서 등록하려고 하는 multipartFile 형식의 이미지 리스트
	 */
	private List<MultipartFile> uploadImages;

	/**
	 * 리뷰 평균
	 */
	private Double avgRating;
	
	/**
	 * 리뷰 갯수
	 */
	private Integer reviewCount;

	private Integer recentCount;

	public ReviewDTO() {
		super(RootType.REVIEW);
	}
}
