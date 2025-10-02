package com.jslhrd.yorimichi.service.manager;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jslhrd.yorimichi.domain.NotificationDTO;
import com.jslhrd.yorimichi.service.NotificationService;

@Service
public class NotificationManager implements NotificationService {

    @Override
    public List<NotificationDTO> findAllByUserId(Long userId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void read(Long notificationId) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void save(NotificationDTO dto) {
        // TODO Auto-generated method stub
        
    }
    
}
