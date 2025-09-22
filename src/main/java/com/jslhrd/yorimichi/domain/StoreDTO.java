package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

/**
 * 상점 DTO.
 *
 * <br>상점 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Getter
@Setter
@Alias("StoreDTO")
public class StoreDTO extends RootDTO {

	/** FK: 주소 ID */
    private Long addressId;

	/** 상점 이름 */
    private String name;

	/** 상점 설명 */
    private String description;

	/** 상점 번호 */
    private String phone;
}