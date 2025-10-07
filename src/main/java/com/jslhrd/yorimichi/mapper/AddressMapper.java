package com.jslhrd.yorimichi.mapper;

import com.jslhrd.yorimichi.domain.AddressDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

/**
 * 주소 Mapper.
 *
 * <br>주소 정보를 전달합니다.
 *
 * @author GeonHoKoo
 * @author LancerAlert
 * @since 1.0
 */
@Mapper
public interface AddressMapper {

	/**
	 * 주소 추가.
	 *
	 * @return 영향 행 수 (추가 1, 그 외 0)
	 */
	int insert(AddressDTO address);

	/**
	 * 주소 단건 조회.
	 *
	 * @param addressId 주소 ID
	 * @return 존재하면 DTO를 담은 Optional, 없으면 Optional.empty()
	 */
	Optional<AddressDTO> selectById(@Param("addressId") Long addressId);

	/**
	 * 주소 수정.
	 *
	 * @return 영향 행 수 (수정 1, 대상 없음 0)
	 */
	int update(AddressDTO address);

	/**
	 * 주소 삭제.
	 *
	 * @param addressId 주소 ID
	 * @return 영향 행 수 (삭제 1, 대상 없음 0)
	 */
	int deleteById(@Param("addressId") Long addressId);

	boolean existsById(@Param("addressId") Long addressId);
}