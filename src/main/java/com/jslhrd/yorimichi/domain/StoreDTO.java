package com.jslhrd.yorimichi.domain;

import lombok.*;

@Getter
@Setter
public class StoreDTO {
    private Long storeId;
    private Long addressId;
    private String name;
    private String description; // NULL 가능
    private String phone;    
}
