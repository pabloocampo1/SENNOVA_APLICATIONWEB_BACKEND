package com.example.sennova.domain.port;

import com.example.sennova.infrastructure.persistence.entities.Notifications;

import java.util.List;

public interface NotificationsPort {

    Notifications save(Notifications notifications);
    List<Notifications> findAllByTargetRoleAndOrderByDateDesc(List<String> tags);
    void deleteNotification(Long id);
    List<Notifications> getAll();
}
