package com.jslhrd.yorimichi.service.manager;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jslhrd.yorimichi.domain.CoeatDTO;
import com.jslhrd.yorimichi.domain.CoeatRequestDTO;
import com.jslhrd.yorimichi.service.CoeatService;

@Service
public class CoeatManager implements CoeatService{

    @Override
    public void acceptParticipant(Principal principal, CoeatRequestDTO coeatRequest) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteCoeat(Long feedId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public CoeatDTO getCoeatDetail(Long feedId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<CoeatDTO> getCoeatList() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void joinCoeat(Long userId, Long feedId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void rejectParticipant(Principal principal, CoeatRequestDTO coeatRequest) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void save(Object dto) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void updateCoeat(Object dto) {
        // TODO Auto-generated method stub
        
    }

}
