package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentUsageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsageEquipmentRepositoryJpa extends JpaRepository<EquipmentUsageEntity, Long> {

    List<EquipmentUsageEntity> findAllByUsageNameContainingIgnoreCase(String name);
}
