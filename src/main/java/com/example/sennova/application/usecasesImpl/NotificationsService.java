package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.UserDtos.UserResponse;
import com.example.sennova.application.usecases.UserUseCase;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.domain.port.NotificationsPort;
import com.example.sennova.infrastructure.persistence.entities.Notifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationsService {

    private final UserUseCase userUseCase;
    private final NotificationsPort notificationsPort;

    @Autowired
    public NotificationsService(UserUseCase userUseCase, NotificationsPort notificationsPort) {
        this.userUseCase = userUseCase;
        this.notificationsPort = notificationsPort;
    }

    public void saveNotification(Notifications notifications, Long userId){
        UserResponse user = this.userUseCase.findById(userId);

        notifications.setActorUser(user.name());
        notifications.setImageUser(notifications.getImageUser());

        Notifications notification = this.notificationsPort.save(notifications);
        System.out.println("NUEVA NOTIFICACION: " + notification);
    }

    public void  deleteByDateBefore(){
        List<Notifications> allNotifications = this.notificationsPort.getAll();

        // addd logic
        for (Notifications notification : allNotifications) {

        }

    }


}
