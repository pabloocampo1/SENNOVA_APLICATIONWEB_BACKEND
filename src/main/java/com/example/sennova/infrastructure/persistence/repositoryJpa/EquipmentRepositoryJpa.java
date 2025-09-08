package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EquipmentRepositoryJpa extends JpaRepository<EquipmentEntity, Long> {

    boolean existsBySerialNumber(Long serialNumber);


    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM EquipmentEntity e WHERE e.internalCode = :internalCode")
    boolean existByInternalCode(@Param("internalCode") String InternalCode);
}
