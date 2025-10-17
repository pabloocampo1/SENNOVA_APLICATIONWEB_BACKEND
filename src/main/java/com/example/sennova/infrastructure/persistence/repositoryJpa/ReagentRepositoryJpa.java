package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.LocationEntity;
import com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities.ReagentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReagentRepositoryJpa extends JpaRepository<ReagentsEntity, Long> {


    List<ReagentsEntity> findAllByReagentNameContainingIgnoreCase(String name);
    List<ReagentsEntity> findAllBySenaInventoryTagContainingIgnoreCase(String tag);
  //  List<ReagentsEntity> findAllByInternalCodeContainingIgnoreCase(String tag);
    List<ReagentsEntity> findAllByLocation(LocationEntity locationEntity);
    List<ReagentsEntity> findAllByExpirationDate(LocalDate expirationDate);

}
