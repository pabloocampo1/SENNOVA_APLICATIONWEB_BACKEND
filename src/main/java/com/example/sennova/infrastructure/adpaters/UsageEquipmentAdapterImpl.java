package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.domain.model.EquipmentUsageModel;
import com.example.sennova.domain.port.UsageEquipmentPersistencePort;
import com.example.sennova.infrastructure.mapperDbo.UsageEquipmentMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.UsageEntity;
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
        UsageEntity equipmentLocationEntity = this.usageEquipmentMapperDbo.toEntity(equipmentUsageModel);
        UsageEntity usageEntitySaved = this.usageEquipmentRepositoryJpa.save(equipmentLocationEntity);
        return this.usageEquipmentMapperDbo.toModel(usageEntitySaved);
    }

    @Override
    public EquipmentUsageModel update(EquipmentUsageModel equipmentUsageModel, Long id) {

        // get the original entity for get data important
        UsageEntity usageEntityOriginal = this.usageEquipmentRepositoryJpa.findById(id)
                .orElseThrow();

        // change the model to entity
        UsageEntity usageEntity = this.usageEquipmentMapperDbo.toEntity(equipmentUsageModel);
        usageEntity.setCreateAt(usageEntityOriginal.getCreateAt());

        UsageEntity usageEntitySaved = this.usageEquipmentRepositoryJpa.save(usageEntity);
        return this.usageEquipmentMapperDbo.toModel(usageEntitySaved);
    }

    @Override
    public List<EquipmentUsageModel> findAll() {
        List<UsageEntity> usageEntityList = this.usageEquipmentRepositoryJpa.findAll();
        return usageEntityList.stream().map(this.usageEquipmentMapperDbo::toModel).toList();
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
        List<UsageEntity> usageEntityList = this.usageEquipmentRepositoryJpa.findAllByUsageNameContainingIgnoreCase(name);
        return usageEntityList.stream().map(this.usageEquipmentMapperDbo::toModel).toList();
    }

    @Override
    public EquipmentUsageModel findById(Long id) {
       UsageEntity usageEntity =  this.usageEquipmentRepositoryJpa.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("No se pudo encontrar el uso,  con id : " + id));
        return this.usageEquipmentMapperDbo.toModel(usageEntity);
    }

    @Override
    public void deleteById(Long id) {
            this.usageEquipmentRepositoryJpa.deleteById(id);
    }
}
