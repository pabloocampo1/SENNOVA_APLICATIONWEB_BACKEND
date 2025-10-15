package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.domain.model.LocationModel;
import com.example.sennova.domain.model.ReagentModel;
import com.example.sennova.domain.port.ReagentPersistencePort;
import com.example.sennova.infrastructure.mapperDbo.LocationMapperDbo;
import com.example.sennova.infrastructure.mapperDbo.ReagentMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.LocationEntity;
import com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities.ReagentsEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.ReagentRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReagentAdapterImpl implements ReagentPersistencePort {

    private final ReagentRepositoryJpa reagentRepositoryJpa;
    private final ReagentMapperDbo reagentMapperDbo;
    private final LocationMapperDbo locationMapperDbo;

    @Autowired
    public ReagentAdapterImpl(ReagentRepositoryJpa reagentRepositoryJpa, ReagentMapperDbo reagentMapperDbo, LocationMapperDbo locationMapperDbo) {
        this.reagentRepositoryJpa = reagentRepositoryJpa;
        this.reagentMapperDbo = reagentMapperDbo;
        this.locationMapperDbo = locationMapperDbo;
    }


    @Override
    public ReagentModel save(ReagentModel reagentModel) {
        ReagentsEntity reagentsEntity = this.reagentRepositoryJpa.save(this.reagentMapperDbo.toEntity(reagentModel));
        return this.reagentMapperDbo.toModel(reagentsEntity);
    }

    @Override
    public ReagentModel update(ReagentModel reagentModel) {
        return null;
    }

    @Override
    public List<ReagentModel> findAll() {
        List<ReagentsEntity> reagentsEntities = this.reagentRepositoryJpa.findAll();
        return reagentsEntities.stream().map(this.reagentMapperDbo::toModel).toList();
    }

    @Override
    public Page<ReagentModel> findAll(Pageable pageable) {
        Page<ReagentsEntity> reagentsEntities = this.reagentRepositoryJpa.findAll(pageable);
        return reagentsEntities.map(this.reagentMapperDbo::toModel);
    }

    @Override
    public List<ReagentModel> findAllByName(String name) {
        List<ReagentsEntity> reagentsEntities = this.reagentRepositoryJpa.findAllByReagentNameContainingIgnoreCase(name);
        System.out.println(reagentsEntities);
        return reagentsEntities.stream().map(this.reagentMapperDbo::toModel).toList();
    }

    @Override
    public List<ReagentModel> findAllByLocation(LocationModel locationModel) {
        LocationEntity locationEntity = this.locationMapperDbo.toEntity(locationModel);
        List<ReagentsEntity> reagentsEntities = this.reagentRepositoryJpa.findAllByLocation(locationEntity);
        return reagentsEntities.stream().map(this.reagentMapperDbo::toModel).toList();

    }

    @Override
    public List<ReagentModel> findAllByInventoryTag(String inventoryTag) {
        List<ReagentsEntity> reagentsEntities = this.reagentRepositoryJpa.findAllBySenaInventoryTagContainingIgnoreCase(inventoryTag);
        return reagentsEntities.stream().map(this.reagentMapperDbo::toModel).toList();
    }

    @Override
    public ReagentModel findById(Long id) {
        ReagentsEntity reagentsEntity = this.reagentRepositoryJpa.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se pudo contrar el reactivo"));

        return this.reagentMapperDbo.toModel(reagentsEntity);
    }

    @Override
    public ReagentsEntity findEntityById(Long id) {
        return this.reagentRepositoryJpa.findById(id).orElseThrow(() -> new IllegalArgumentException("No se encontro el reactivo"));
    }

    @Override
    public void deleteById(Long id) {
        this.reagentRepositoryJpa.deleteById(id);
    }


}
