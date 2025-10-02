package com.jslhrd.yorimichi.service.manager;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jslhrd.yorimichi.domain.CategoryDTO;
import com.jslhrd.yorimichi.domain.SearchDTO;
import com.jslhrd.yorimichi.service.CategoryService;

@Service
public class CategoryManager implements CategoryService {

    @Override
    public void delete(Long categoryId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<CategoryDTO> findAll(SearchDTO dto) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void linkCategoryToStore(Long storeId, Long categoryId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void save(CategoryDTO dto) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void unlinkCategoryFromStore(Long storeId, Long categoryId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(CategoryDTO dto) {
        // TODO Auto-generated method stub
        
    }

}
