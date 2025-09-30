package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.EquipmentInventory.request.MaintenanceEquipmentRequest;
import com.example.sennova.application.usecases.EquipmentUseCase;
import com.example.sennova.application.usecases.MaintenanceEquipmentUseCase;
import com.example.sennova.domain.model.EquipmentModel;
import com.example.sennova.domain.model.MaintenanceRecordEquipmentModel;
import com.example.sennova.domain.port.MaintenanceEquipmentPersistencePort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintenanceEquipmentServiceImpl implements MaintenanceEquipmentUseCase {

    private final MaintenanceEquipmentPersistencePort maintenanceEquipmentPersistencePort;
    private final EquipmentUseCase equipmentUseCase;

    @Autowired
    public MaintenanceEquipmentServiceImpl(MaintenanceEquipmentPersistencePort maintenanceEquipmentPersistencePort, EquipmentUseCase equipmentUseCase) {
        this.maintenanceEquipmentPersistencePort = maintenanceEquipmentPersistencePort;
        this.equipmentUseCase = equipmentUseCase;
    }

    @Override
    public MaintenanceRecordEquipmentModel save(MaintenanceEquipmentRequest maintenanceEquipmentRequest) {
        EquipmentModel equipmentModel = this.equipmentUseCase.getById(maintenanceEquipmentRequest.equipmentId());
        MaintenanceRecordEquipmentModel maintenanceRecordEquipmentModel = new MaintenanceRecordEquipmentModel();
        maintenanceRecordEquipmentModel.setEquipment(equipmentModel);
        maintenanceRecordEquipmentModel.setDateMaintenance(maintenanceEquipmentRequest.dateMaintenance());
        maintenanceRecordEquipmentModel.setMaintenanceType(maintenanceEquipmentRequest.maintenanceType());
        maintenanceRecordEquipmentModel.setPerformedBy(maintenanceEquipmentRequest.performedBy());
        maintenanceRecordEquipmentModel.setNotes(maintenanceEquipmentRequest.notes());


        // change the maintenance date for the same day but the next year


        return this.maintenanceEquipmentPersistencePort.save(maintenanceRecordEquipmentModel);
    }

    @Override
    public List<MaintenanceRecordEquipmentModel> getAllByEquipmentId(Long equipmentId) {
        return this.maintenanceEquipmentPersistencePort.findAllByEquipmentId(equipmentId);
    }

    @Override
    public Boolean deleteById(Long id) {
        return this.maintenanceEquipmentPersistencePort.deleteById(id);
    }
}
