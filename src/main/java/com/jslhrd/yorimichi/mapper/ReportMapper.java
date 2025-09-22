package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.ReportDTO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 신고 Mapper.
 *
 * <br>신고 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface ReportMapper {

	/**
	 * 신고 추가.
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(ReportDTO dto);

	/**
	 * 신고 삭제.
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int delete(ReportDTO dto);
}