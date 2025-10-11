package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.ReportDTO;
import com.jslhrd.yorimichi.enums.ReportStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

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

	List<ReportDTO> selectAll();

	Optional<ReportStatus> selectStatus(@Param("reportId") Long reportId);

	boolean isReporter(@Param("userId") Long userId,
	                   @Param("reportId") Long reportId);

	/**
	 * 신고 추가.
	 *
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(ReportDTO report);

	int updateStatus(@Param("reportId") Long reportId,
	                 @Param("cur") ReportStatus cur,
	                 @Param("to") ReportStatus to);

	/**
	 * 신고 삭제.
	 *
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int cancelByReporter(@Param("userId") Long userId,
	                     @Param("reportId") Long reportId,
	                     @Param("cur") ReportStatus cur);
}