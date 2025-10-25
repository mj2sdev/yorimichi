// StationService.java
package com.jslhrd.yorimichi.service;

import com.jslhrd.yorimichi.domain.response.SliceResponse;
import com.jslhrd.yorimichi.domain.response.StationDTO;

public interface StationService {
	SliceResponse<StationDTO> findSlice(Long rootId, int size);
}
