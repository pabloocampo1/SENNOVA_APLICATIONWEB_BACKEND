package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.domain.model.EquipmentUsageModel;
import com.example.sennova.domain.port.UsageEquipmentPersistencePort;
import com.example.sennova.infrastructure.mapperDbo.UsageEquipmentMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentLocationEntity;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentUsageEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.UsageEquipmentRepositoryJpa;
import com.example.sennova.web.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UsageEquipmentAdapterImpl implements UsageEquipmentPersistencePort {
    private final UsageEquipmentMapperDbo usageEquipmentMapperDbo;
    private final UsageEquipmentRepositoryJpa usageEquipmentRepositoryJpa;

    @Autowired
    public UsageEquipmentAdapterImpl(UsageEquipmentMapperDbo usageEquipmentMapperDbo, UsageEquipmentRepositoryJpa usageEquipmentRepositoryJpa) {
        this.usageEquipmentMapperDbo = usageEquipmentMapperDbo;
        this.usageEquipmentRepositoryJpa = usageEquipmentRepositoryJpa;
    }

    @Override
    public EquipmentUsageModel save(@Valid EquipmentUsageModel equipmentUsageModel) {
        EquipmentUsageEntity equipmentLocationEntity = this.usageEquipmentMapperDbo.toEntity(equipmentUsageModel);
        EquipmentUsageEntity equipmentUsageEntitySaved = this.usageEquipmentRepositoryJpa.save(equipmentLocationEntity);
        return this.usageEquipmentMapperDbo.toModel(equipmentUsageEntitySaved);
    }

    @Override
    public EquipmentUsageModel update(EquipmentUsageModel equipmentUsageModel, Long id) {

        // get the original entity for get data important
        EquipmentUsageEntity equipmentUsageEntityOriginal = this.usageEquipmentRepositoryJpa.findById(id)
                .orElseThrow();


        // change the model to entity
        EquipmentUsageEntity equipmentUsageEntity = this.usageEquipmentMapperDbo.toEntity(equipmentUsageModel);
        equipmentUsageEntity.setCreateAt(equipmentUsageEntityOriginal.getCreateAt());

        EquipmentUsageEntity equipmentUsageEntitySaved = this.usageEquipmentRepositoryJpa.save(equipmentUsageEntity);
        return this.usageEquipmentMapperDbo.toModel(equipmentUsageEntitySaved);
    }

    @Override
    public List<EquipmentUsageModel> findAll() {
        List<EquipmentUsageEntity> equipmentUsageEntityList = this.usageEquipmentRepositoryJpa.findAll();
        return equipmentUsageEntityList.stream().map(this.usageEquipmentMapperDbo::toModel).toList();
    }

    @Override
    public Page<EquipmentUsageModel> findAll(Pageable pageable) {
        return this.usageEquipmentRepositoryJpa.findAll(pageable).map(this.usageEquipmentMapperDbo::toModel);
    }

    @Override
    public boolean existsById(Long id) {
        return this.usageEquipmentRepositoryJpa.existsById(id);
    }

    @Override
    public List<EquipmentUsageModel> findAllByName(String name) {
        List<EquipmentUsageEntity> equipmentUsageEntityList = this.usageEquipmentRepositoryJpa.findAllByUsageNameContainingIgnoreCase(name);
        return equipmentUsageEntityList.stream().map(this.usageEquipmentMapperDbo::toModel).toList();
    }

    @Override
    public EquipmentUsageModel findById(Long id) {
       EquipmentUsageEntity equipmentUsageEntity =  this.usageEquipmentRepositoryJpa.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("No se pudo encontrar el elemento con id : " + id));
        return this.usageEquipmentMapperDbo.toModel(equipmentUsageEntity);
    }

    @Override
    public void deleteById(Long id) {
            this.usageEquipmentRepositoryJpa.deleteById(id);
    }
}
