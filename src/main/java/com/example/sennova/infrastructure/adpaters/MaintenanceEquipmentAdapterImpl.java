package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.domain.model.MaintenanceRecordEquipmentModel;
import com.example.sennova.domain.port.MaintenanceEquipmentPersistencePort;
import com.example.sennova.infrastructure.mapperDbo.MaintenanceMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.MaintenanceRecordsEquipment;
import com.example.sennova.infrastructure.persistence.repositoryJpa.MaintenanceEquipmentRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MaintenanceEquipmentAdapterImpl implements MaintenanceEquipmentPersistencePort {

    private final MaintenanceEquipmentRepositoryJpa maintenanceEquipmentRepositoryJpa;
    private final MaintenanceMapperDbo maintenanceMapperDbo;

    @Autowired
    public MaintenanceEquipmentAdapterImpl(MaintenanceEquipmentRepositoryJpa maintenanceEquipmentRepositoryJpa, MaintenanceMapperDbo maintenanceMapperDbo) {
        this.maintenanceEquipmentRepositoryJpa = maintenanceEquipmentRepositoryJpa;
        this.maintenanceMapperDbo = maintenanceMapperDbo;
    }


    @Override
    public MaintenanceRecordEquipmentModel save(MaintenanceRecordEquipmentModel maintenanceRecordEquipmentModel) {
        MaintenanceRecordsEquipment maintenanceRecordsEquipment = this.maintenanceMapperDbo.toEntity(maintenanceRecordEquipmentModel);
        MaintenanceRecordsEquipment maintenanceRecordsEquipmentSaved = this.maintenanceEquipmentRepositoryJpa.save(maintenanceRecordsEquipment);
        return this.maintenanceMapperDbo.toModel(maintenanceRecordsEquipmentSaved);
    }

    @Override
    public List<MaintenanceRecordEquipmentModel> findAllByEquipmentId(Long equipmentId) {
            List<MaintenanceRecordsEquipment> maintenanceRecordsEquipmentPage = this.maintenanceEquipmentRepositoryJpa.findByEquipment_EquipmentId(equipmentId);
            return  maintenanceRecordsEquipmentPage.stream().map(this.maintenanceMapperDbo::toModel).toList();
    }

    @Override
    public Boolean existById(Long id) {
        return this.maintenanceEquipmentRepositoryJpa.existsById(id);
    }

    @Override
    public Boolean deleteById(Long id) {
        if(!this.maintenanceEquipmentRepositoryJpa.existsById(id)){
            throw new IllegalArgumentException("No se encontro el mantenimiento con id: " + id);
        }

        this.maintenanceEquipmentRepositoryJpa.deleteById(id);
        return true;
    }

    @Override
    public void deleteByEquipmentId(Long equipmentId) {
            this.maintenanceEquipmentRepositoryJpa.deleteByEquipmentId(equipmentId);
    }
}
