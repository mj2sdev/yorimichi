package com.jslhrd.yorimichi.service.manager;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.service.RelationshipService;

@Service
public class RelationshipManager implements RelationshipService {

    @Override
    public void delete(Principal principal, Long userId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void deleteBlock(Principal principal, Long userId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<UserDTO> findFollowById(Principal principal) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<UserDTO> findFollowerById(Principal principal) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void saveBlock(Principal principal, Long userId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void saveFollow(Principal principal, Long userId) {
        // TODO Auto-generated method stub
        
    }

}
