package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.domain.model.EquipmentLocationModel;
import com.example.sennova.domain.model.EquipmentModel;
import com.example.sennova.domain.model.EquipmentUsageModel;
import com.example.sennova.domain.port.EquipmentPersistencePort;
import com.example.sennova.infrastructure.mapperDbo.EquipmentMapperDbo;
import com.example.sennova.infrastructure.mapperDbo.EquipmentMapperDboImpl;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.EquipmentRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EquipmentAdapterImpl implements EquipmentPersistencePort {

    private final EquipmentRepositoryJpa equipmentRepositoryJpa;
    private final EquipmentMapperDbo equipmentMapperDbo;

    @Autowired
    public EquipmentAdapterImpl(EquipmentRepositoryJpa equipmentRepositoryJpa, EquipmentMapperDbo equipmentMapperDbo) {
        this.equipmentRepositoryJpa = equipmentRepositoryJpa;
        this.equipmentMapperDbo = equipmentMapperDbo;
    }

    @Override
    public EquipmentModel save(EquipmentModel equipmentModel) {
        EquipmentEntity equipmentEntity = this.equipmentMapperDbo.toEntity(equipmentModel);
        return this.equipmentMapperDbo.toModel(this.equipmentRepositoryJpa.save(equipmentEntity));
    }

    @Override
    public EquipmentModel update(Long id, EquipmentModel equipmentModel) {
        return null;
    }

    @Override
    public Page<EquipmentModel> getAllPage(Pageable pageable) {
        return null;
    }

    @Override
    public EquipmentModel findById(Long id) {
        return null;
    }

    @Override
    public EquipmentModel findByInternalCode(Long internalCode) {
        return null;
    }

    @Override
    public List<EquipmentModel> findAllByName(String name) {
        return List.of();
    }

    @Override
    public Boolean existById(Long id) {
        return null;
    }

    @Override
    public void changeState(Long id, String state) {

    }

    @Override
    public List<EquipmentModel> findAllByLocation(EquipmentLocationModel equipmentLocationModel) {
        return List.of();
    }

    @Override
    public List<EquipmentModel> findAllByUsage(EquipmentUsageModel equipmentUsageModel) {
        return List.of();
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Boolean existsBySerialNumber(Long serialNumber) {
        return this.equipmentRepositoryJpa.existsBySerialNumber(serialNumber);
    }

    @Override
    public Boolean existsByInternalCode(String internalCode) {
        return this.equipmentRepositoryJpa.existByInternalCode(internalCode);
    }
}
