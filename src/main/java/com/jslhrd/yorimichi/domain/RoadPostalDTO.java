package com.jslhrd.yorimichi.domain;

import lombok.*;

@Getter
@Setter
public class RoadPostalDTO {
    private Long roadId;    // FK → road.id
    private Long postalId;  // FK → postal.id    
}
