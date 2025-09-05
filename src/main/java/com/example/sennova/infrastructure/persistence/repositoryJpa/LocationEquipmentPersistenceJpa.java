package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.domain.model.EquipmentLocationModel;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentLocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationEquipmentPersistenceJpa extends JpaRepository<EquipmentLocationEntity, Long> {
    List<EquipmentLocationEntity> findAllByLocationNameContainingIgnoreCase(String name);
}
