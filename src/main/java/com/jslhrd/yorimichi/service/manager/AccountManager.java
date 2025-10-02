package com.jslhrd.yorimichi.service.manager;

import org.springframework.stereotype.Service;

import com.jslhrd.yorimichi.domain.UserDTO;
import com.jslhrd.yorimichi.service.AccountService;


@Service
public class AccountManager implements AccountService {

    @Override
    public void changePassword(UserDTO dto) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void delete(Long userId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void signup(UserDTO dto) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void signupSocial(String token) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean validateNickname(String nickname) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean verificateEmail(String email) {
        // TODO Auto-generated method stub
        return false;
    }
    
}
