package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.StoreDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 북마크(즐겨찾기) Mapper.
 *
 * <br>북마크(즐겨찾기) 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface BookmarkMapper {

	List<StoreDTO> selectByUserId(@Param("userId") Long userId);

	/**
	 * 북마크(즐겨찾기) 추가.
	 *
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(@Param("userId") Long userId,
	           @Param("storeId") Long storeId);

	/**
	 * 북마크(즐겨찾기) 삭제.
	 *
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int delete(@Param("userId") Long userId,
	           @Param("storeId") Long storeId);
}