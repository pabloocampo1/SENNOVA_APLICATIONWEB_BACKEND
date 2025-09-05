package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentUsageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsageEquipmentRepositoryJpa extends JpaRepository<EquipmentUsageEntity, Long> {
}
