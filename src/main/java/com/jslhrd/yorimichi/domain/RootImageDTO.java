package com.jslhrd.yorimichi.domain;

import lombok.*;


@Getter
@Setter
public class RootImageDTO  {
    private Long rootId;  // FK → root.root_id
    private Long imageId; // FK → image.image_id

}
