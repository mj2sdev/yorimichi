package com.jslhrd.yorimichi.domain;

import com.jslhrd.yorimichi.enums.RootType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 리뷰 DTO.
 *
 * <br>리뷰 정보를 전닿합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @author mj2sdev
 * 
 * @version 1.0 초안 작성
 * @version 1.1
 * <ol>
 * <li> 주석 정리 제안
 * <li> store에서 필요한 리뷰관련 데이터 변수 추가
 * @version 1.2
 * <ol>
 * <li> 리뷰 평균, 리뷰 갯수 등 필드 추가
 * <li> 변수명 충돌 해결
 */
@Getter
@Setter
@ToString(callSuper = true)
public class ReviewDTO extends RootDTO implements FeedDTO {
	/** 유저 ID (FORIGN KEY) */
	private Long userId;
	/** 상점 ID (FORIGN KEY) */
	private Long storeId;
	/** 리뷰 평점 */
	private Integer rating;
	/** 리뷰 내용 */
	private String content;

	private Long receiptImageId;

	private Boolean receiptStatus;
	/** 리뷰를 작성한 유저 */
	private UserDTO user;
	/** 리뷰가 작성된 상점 */
	private StoreDTO store;
	/** 리뷰의 메뉴 목록 */
	private List<FoodDTO> foods;
	/** 리뷰 이미지 목록 */
	private List<ImageDTO> images;
	/** front에서 등록하려고 하는 multipartFile 형식의 이미지 리스트 */
	private List<MultipartFile> uploadImages;

	private MultipartFile uploadReceipt;

	private ImageDTO receipt;

	/// store detail 에서 사용됨
	/** 리뷰 평균 */
	private Double avgRating;
	/** 리뷰 갯수 */
	private Integer reviewCount;
	/** 최근 갯수 */
	private Integer recentCount;

	public ReviewDTO() { super(RootType.REVIEW); }
}
