package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.Notification;
import com.makersacademy.acebook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(User user);
}