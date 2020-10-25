package com.webapp.notifier.service;

import com.webapp.notifier.model.Notification;
import com.webapp.notifier.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository repository;

    public void save(Notification n){
        repository.save(n);
    }

    public Notification getNotificationByID(Long id){
        return repository.findById(id).orElse(null);
    }
}
