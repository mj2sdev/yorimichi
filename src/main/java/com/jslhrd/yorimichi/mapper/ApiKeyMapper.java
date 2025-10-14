package com.jslhrd.yorimichi.mapper;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.jslhrd.yorimichi.domain.ApiKeyDTO;

@Mapper
public interface ApiKeyMapper {
	
	public Optional<String> select(ApiKeyDTO dto);
}
