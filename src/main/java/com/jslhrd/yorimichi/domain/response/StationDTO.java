package com.jslhrd.yorimichi.domain.response;

import com.jslhrd.yorimichi.domain.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StationDTO extends RootDTO {

	private UserDTO user;

	private StoreDTO store;

	private AddressDTO address;

	private ReviewDTO review;

	private ImageDTO image;

	private CoeatDTO coeat;
}