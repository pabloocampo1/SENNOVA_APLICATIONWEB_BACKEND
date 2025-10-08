package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.UsageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsageEquipmentRepositoryJpa extends JpaRepository<UsageEntity, Long> {

    List<UsageEntity> findAllByUsageNameContainingIgnoreCase(String name);
}
