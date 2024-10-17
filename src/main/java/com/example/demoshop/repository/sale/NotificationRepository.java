package com.example.demoshop.repository.sale;

import com.example.demoshop.domain.transaction.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserEmail(String userEmail);
}
