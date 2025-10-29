package com.jslhrd.yorimichi.domain;

import com.jslhrd.yorimichi.enums.RootType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 상점 DTO.
 *
 * <br>상점 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 * 
 * @author
 * @version 1.1
 * <ol>
 * <li>주석 정리 제안
 * <li>bookmark, like, keywords 등 store Detail에 필요한 항목 추가
 */
@Getter
@Setter
@ToString(callSuper = true)
public class StoreDTO extends RootDTO {

	/** 주소 ID (FORIGN KEY) */
	private Long addressId;
	/** 상점 이름 */
	private String name;
	/** 상점 설명 */
	private String description;
	/** 상점 번호 */
	private String phone;
	/** 리뷰 AI 요약 */
	private String summaredReview;
	/** 리뷰 통계관련 */
	private ReviewDTO review;
	/** 주소 */
	private AddressDTO address;
	/** 좋아요 */
	private LikeDTO like;
	/** 북마크 */
	private BookmarkDTO bookmark;
	/** 음식 카테고리  목록 */
	private List<CategoryDTO> categories;
	/** 시설 카테고리 목록 */
	private List<FacilityCategoryDTO> facilities;
	/** 음식 목록 */
	private List<FoodDTO> foods;
	/** 리뷰 목록 */
	private List<ReviewDTO> reviews;
	/** 같이 먹기 목록 */
	private List<CoeatDTO> coeats;
	/** 상점 이미지 목록 */
	private List<ImageDTO> images;
	/** 키워드 목록 */
	private List<KeywordDTO> keywords;

	private CoeatDTO coeat;

	private ImageDTO image;

	public StoreDTO() {
		super(RootType.STORE);
	}
}
