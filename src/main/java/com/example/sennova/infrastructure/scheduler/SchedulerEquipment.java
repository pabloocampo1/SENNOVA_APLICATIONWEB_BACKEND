package com.example.sennova.infrastructure.scheduler;

import com.example.sennova.application.dto.UserDtos.UserResponse;
import com.example.sennova.application.usecases.EquipmentUseCase;
import com.example.sennova.application.usecases.UserUseCase;
import com.example.sennova.domain.model.EquipmentModel;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.infrastructure.restTemplate.EquipmentEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SchedulerEquipment {

    private final EquipmentUseCase equipmentUseCase;
    private final UserUseCase userUseCase;
    private final EquipmentEmail equipmentEmail;

    @Autowired
    public SchedulerEquipment(EquipmentUseCase equipmentUseCase, UserUseCase userUseCase, EquipmentEmail equipmentEmail) {
        this.equipmentUseCase = equipmentUseCase;
        this.userUseCase = userUseCase;
        this.equipmentEmail = equipmentEmail;
    }

    // @Scheduled(cron = "0 */1 * * * ?")
    @Scheduled(cron = "0 0 4 * * ?")
    public void checkMaintenanceEquipment() {
        List<EquipmentModel> equipments = this.equipmentUseCase.getAllEquipmentToMaintenance();

        if (equipments.isEmpty()) {
            return;
        }

        List<UserModel> users = this.userUseCase.findAllModels();

        users.stream()
                .filter(UserModel::isNotifyEquipment)
                .forEach(user -> {
                    String email = user.getEmail();
                    try {
                        this.equipmentEmail.sendEmailMaintenance(email, equipments);
                    } catch (Exception e) {
                        System.err.println("Error al enviar correo a " + email + ": " + e.getMessage());
                    }
                });
    }


}
