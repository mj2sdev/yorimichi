package com.jslhrd.yorimichi.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * 권한/역할
 */
@Getter
@Setter
public class RoleDTO {
    private Long id;     // PK
    private String name; // 역할명
}
