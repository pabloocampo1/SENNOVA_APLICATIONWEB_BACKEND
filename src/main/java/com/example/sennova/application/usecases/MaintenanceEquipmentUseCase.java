package com.example.sennova.application.usecases;

import com.example.sennova.application.dto.EquipmentInventory.MaintenanceEquipmentRequest;
import com.example.sennova.domain.model.MaintenanceRecordEquipmentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MaintenanceEquipmentUseCase {
    MaintenanceRecordEquipmentModel save(MaintenanceEquipmentRequest maintenanceEquipmentRequest);
    List<MaintenanceRecordEquipmentModel> getAllByEquipmentId(Long equipmentId);
    Boolean deleteById(Long id);
}
