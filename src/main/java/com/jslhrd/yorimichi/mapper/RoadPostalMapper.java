package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.RoadPostalDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 도로명 - 우편번호 매핑 Mapper.
 *
 * <br>도로명 - 우편번호 매핑 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface RoadPostalMapper {

	/**
	 * 도로명 - 우편번호 매핑 생성.
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(RoadPostalDTO dto);

	/**
	 * 도로명 - 우편번호 매핑 삭제.
	 * @return 영향 생 수 (삭제 1, 대상 없음 0)
	 */
	int delete(RoadPostalDTO dto);
}