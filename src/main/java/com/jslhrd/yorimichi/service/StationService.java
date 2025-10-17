// StationService.java
package com.jslhrd.yorimichi.service;

import com.jslhrd.yorimichi.domain.CoeatDTO;
import com.jslhrd.yorimichi.domain.ReviewDTO;
import java.util.List;

public interface StationService {
    List<ReviewDTO> findLatestReviews(int count);
    List<CoeatDTO> findLatestCoeats(int count);
}
