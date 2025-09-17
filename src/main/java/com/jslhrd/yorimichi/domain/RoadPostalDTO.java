package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 도로-우편번호 매핑
 * - (road_id, postal_id) 복합키 성격
 */
@Getter
@Setter
public class RoadPostalDTO {
    private Long roadId;    // FK → road.id
    private Long postalId;  // FK → postal.id
}
