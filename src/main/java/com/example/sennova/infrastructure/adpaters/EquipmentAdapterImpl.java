package com.example.sennova.infrastructure.adpaters;

import com.example.sennova.domain.constants.EquipmentConstants;
import com.example.sennova.domain.model.EquipmentLocationModel;
import com.example.sennova.domain.model.EquipmentModel;
import com.example.sennova.domain.model.EquipmentUsageModel;
import com.example.sennova.domain.port.EquipmentPersistencePort;
import com.example.sennova.infrastructure.mapperDbo.EquipmentMapperDbo;
import com.example.sennova.infrastructure.mapperDbo.EquipmentMapperDboImpl;
import com.example.sennova.infrastructure.mapperDbo.LocationEquipmentMapperDbo;
import com.example.sennova.infrastructure.mapperDbo.UsageEquipmentMapperDbo;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentEntity;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentLocationEntity;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentUsageEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.EquipmentRepositoryJpa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class EquipmentAdapterImpl implements EquipmentPersistencePort {

    private final EquipmentRepositoryJpa equipmentRepositoryJpa;
    private final EquipmentMapperDbo equipmentMapperDbo;
    private final UsageEquipmentMapperDbo usageEquipmentMapperDbo;
    private final LocationEquipmentMapperDbo locationEquipmentMapperDbo;

    @Autowired
    public EquipmentAdapterImpl(EquipmentRepositoryJpa equipmentRepositoryJpa, EquipmentMapperDbo equipmentMapperDbo, UsageEquipmentMapperDbo usageEquipmentMapperDbo, LocationEquipmentMapperDbo locationEquipmentMapperDbo) {
        this.equipmentRepositoryJpa = equipmentRepositoryJpa;
        this.equipmentMapperDbo = equipmentMapperDbo;
        this.usageEquipmentMapperDbo = usageEquipmentMapperDbo;
        this.locationEquipmentMapperDbo = locationEquipmentMapperDbo;
    }

    @Override
    public EquipmentModel save(EquipmentModel equipmentModel) {
        EquipmentEntity equipmentEntity = this.equipmentMapperDbo.toEntity(equipmentModel);
        return this.equipmentMapperDbo.toModel(this.equipmentRepositoryJpa.save(equipmentEntity));
    }

    @Override
    public EquipmentModel update( EquipmentModel equipmentModel) {
        EquipmentEntity equipmentEntity = this.equipmentRepositoryJpa.findById(equipmentModel.getEquipmentId())
                .orElseThrow(() -> new IllegalArgumentException("No se pudo encontrar el equipo."));



        EquipmentEntity entityToSave = this.equipmentMapperDbo.toEntity(equipmentModel);
        entityToSave.setCreateAt(equipmentEntity.getCreateAt());
        return this.equipmentMapperDbo.toModel(this.equipmentRepositoryJpa.save(entityToSave));
    }

    @Override
    public Page<EquipmentModel> getAllPage(Pageable pageable) {
        Page<EquipmentEntity> equipmentEntities = this.equipmentRepositoryJpa.findAll(pageable);
        return equipmentEntities.map(this.equipmentMapperDbo::toModel);
    }

    @Override
    public EquipmentModel findById(Long id) {
        EquipmentEntity equipmentEntity = this.equipmentRepositoryJpa.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontro el equipo."));
        return this.equipmentMapperDbo.toModel(equipmentEntity);
    }

    @Override
    public List<EquipmentModel> findAllByInternalCode(String internalCode) {
        List<EquipmentEntity> equipmentEntityList = this.equipmentRepositoryJpa.findAllByInternalCodeContainingIgnoreCase(internalCode);
        return equipmentEntityList.stream().map(this.equipmentMapperDbo::toModel).toList();
    }

    @Override
    public List<EquipmentModel> findAll() {
        List<EquipmentEntity> equipmentEntities = this.equipmentRepositoryJpa.findAll();
        return equipmentEntities.stream().map(this.equipmentMapperDbo::toModel).toList();
    }

    @Override
    public List<EquipmentModel> findAllByName(String name) {
        List<EquipmentEntity> equipmentEntityList = this.equipmentRepositoryJpa.findAllByEquipmentNameContainingIgnoreCase(name);
        return equipmentEntityList.stream().map(this.equipmentMapperDbo::toModel).toList();
    }

    @Override
    public Boolean existById(Long id) {
        return this.equipmentRepositoryJpa.existsById(id);
    }

    @Override
    public void changeState(Long id, String state) {
        EquipmentEntity equipmentEntity = this.equipmentRepositoryJpa.findById(id)
                .orElseThrow( () -> new IllegalArgumentException("Error : No se encontro el equipo"));

        if (!state.equals(EquipmentConstants.STATUS_ACTIVE)
                && !state.equals(EquipmentConstants.STATUS_DECOMMISSIONED)
                && !state.equals(EquipmentConstants.STATUS_OUT_OF_SERVICE)) {
            throw new IllegalArgumentException("Estado de equipo inválido. Los valores permitidos son: Activo, Dado de baja, Fuera de servicio.");
        }

        // change the attribute available according the state
        equipmentEntity.setAvailable(!state.equals(EquipmentConstants.STATUS_DECOMMISSIONED) && !state.equals(EquipmentConstants.STATUS_OUT_OF_SERVICE));

    }

    @Override
    public List<EquipmentModel> findAllByLocation(EquipmentLocationModel equipmentLocationModel) {
        EquipmentLocationEntity equipmentLocationEntity = this.locationEquipmentMapperDbo.toEntity(equipmentLocationModel);
        List<EquipmentEntity> equipmentEntityList = this.equipmentRepositoryJpa.findAllByLocation(equipmentLocationEntity);
        return equipmentEntityList.stream().map(this.equipmentMapperDbo::toModel).toList();
    }

    @Override
    public List<EquipmentModel> findAllByUsage(EquipmentUsageModel equipmentUsageModel) {
        EquipmentUsageEntity equipmentUsageEntity = this.usageEquipmentMapperDbo.toEntity(equipmentUsageModel);
        List<EquipmentEntity> equipmentEntityList = this.equipmentRepositoryJpa.findAllByUsage(equipmentUsageEntity);
        return equipmentEntityList.stream().map(this.equipmentMapperDbo::toModel).toList();
    }

    @Override
    public void delete(Long id) {
        this.equipmentRepositoryJpa.deleteById(id);
    }

    @Override
    public Boolean existsBySerialNumber(Long serialNumber) {
        return this.equipmentRepositoryJpa.existsBySerialNumber(serialNumber);
    }

    @Override
    public Boolean existsByInternalCode(String internalCode) {
        return this.equipmentRepositoryJpa.existByInternalCode(internalCode);
    }

    @Override
    public EquipmentEntity findEntityById(Long id) {
        System.out.println("entro a buscarlo");
        return this.equipmentRepositoryJpa.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontro el equipo"));
    }
}
