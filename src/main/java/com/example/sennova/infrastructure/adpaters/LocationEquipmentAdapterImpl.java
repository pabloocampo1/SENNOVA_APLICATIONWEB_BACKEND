package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.domain.model.EquipmentLocationModel;
import com.example.sennova.domain.port.LocationEquipmentPersistencePort;
import com.example.sennova.infrastructure.mapperDbo.LocationEquipmentMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentLocationEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.LocationEquipmentPersistenceJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LocationEquipmentAdapterImpl implements LocationEquipmentPersistencePort {

    private final LocationEquipmentPersistenceJpa locationEquipmentPersistenceJpa;
    private final LocationEquipmentMapperDbo locationEquipmentMapperDbo;

    @Autowired
    public LocationEquipmentAdapterImpl(LocationEquipmentPersistenceJpa locationEquipmentPersistenceJpa, LocationEquipmentMapperDbo locationEquipmentMapperDbo) {
        this.locationEquipmentPersistenceJpa = locationEquipmentPersistenceJpa;
        this.locationEquipmentMapperDbo = locationEquipmentMapperDbo;
    }

    @Override
    public EquipmentLocationModel save(EquipmentLocationModel equipmentLocationModel) {
        EquipmentLocationEntity equipmentLocationEntity = this.locationEquipmentMapperDbo.toEntity(equipmentLocationModel);
        EquipmentLocationEntity equipmentLocationEntitySaved = this.locationEquipmentPersistenceJpa.save(equipmentLocationEntity);
        return this.locationEquipmentMapperDbo.toModel(equipmentLocationEntitySaved);
    }

    @Override
    public EquipmentLocationModel findById(Long id) {
        return this.locationEquipmentMapperDbo.toModel(this.locationEquipmentPersistenceJpa.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontro la ubicacion con id: " + id)));
    }

    @Override
    public EquipmentLocationModel update(EquipmentLocationModel equipmentLocationModel, Long id) {

        // get the original entity to pass important data like created date
        EquipmentLocationEntity equipmentLocationEntityOriginal = this.locationEquipmentPersistenceJpa.findById(id)
                .orElseThrow();

        // mapped the new entity
        EquipmentLocationEntity equipmentLocationEntity = this.locationEquipmentMapperDbo.toEntity(equipmentLocationModel);

        //  add the attributes of the original entity
        equipmentLocationEntity.setCreateAt(equipmentLocationEntityOriginal.getCreateAt());

        // save and return
        EquipmentLocationEntity equipmentLocationEntityUpdate = this.locationEquipmentPersistenceJpa.save(equipmentLocationEntity);
        return this.locationEquipmentMapperDbo.toModel(equipmentLocationEntityUpdate);
    }

    @Override
    public Page<EquipmentLocationModel> findAllPage(Pageable pageable) {
        Page<EquipmentLocationEntity> page = this.locationEquipmentPersistenceJpa.findAll(pageable);
        return page.map(this.locationEquipmentMapperDbo::toModel);
    }

    @Override
    public void deleteById(Long id) {
            this.locationEquipmentPersistenceJpa.deleteById(id);
    }

    @Override
    public Boolean existById(Long id) {
        return this.locationEquipmentPersistenceJpa.existsById(id);
    }

    @Override
    public List<EquipmentLocationModel> findAllByName(String name) {
        List<EquipmentLocationEntity> allByName = this.locationEquipmentPersistenceJpa.findAllByLocationNameContainingIgnoreCase(name);
        return allByName
                .stream()
                .map(this.locationEquipmentMapperDbo::toModel)
                .toList();
    }

    @Override
    public List<EquipmentLocationModel> findAll() {
        List<EquipmentLocationEntity> allEntities = this.locationEquipmentPersistenceJpa.findAll();
        List<EquipmentLocationModel> allModels = allEntities.stream().map(this.locationEquipmentMapperDbo::toModel).toList();
        return allModels;
    }
}
