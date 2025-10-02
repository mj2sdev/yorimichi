package com.jslhrd.yorimichi.service.manager;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jslhrd.yorimichi.domain.ReportDTO;
import com.jslhrd.yorimichi.domain.ReviewDTO;
import com.jslhrd.yorimichi.service.ReviewService;

@Service
public class ReviewManager implements ReviewService {

    @Override
    public void delete(ReviewDTO review, Principal principal) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<ReviewDTO> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ReviewDTO> findAllByStoreId(Long storeId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ReviewDTO findById(Long reviewId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void report(ReportDTO dto) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void save(ReviewDTO dto) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(ReviewDTO dto) {
        // TODO Auto-generated method stub
        
    }

}
