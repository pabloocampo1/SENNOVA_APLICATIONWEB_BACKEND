package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationEquipmentPersistenceJpa extends JpaRepository<LocationEntity, Long> {
    List<LocationEntity> findAllByLocationNameContainingIgnoreCase(String name);
}
