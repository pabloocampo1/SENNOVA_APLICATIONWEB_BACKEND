package com.example.sennova.infrastructure.persistence.repositoryJpa;

import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentEntity;
import com.example.sennova.infrastructure.persistence.entities.LocationEntity;
import com.example.sennova.infrastructure.persistence.entities.UsageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EquipmentRepositoryJpa extends JpaRepository<EquipmentEntity, Long> {

    boolean existsBySerialNumber(Long serialNumber);


    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM EquipmentEntity e WHERE e.internalCode = :internalCode")
    boolean existByInternalCode(@Param("internalCode") String InternalCode);

    List<EquipmentEntity> findAllByLocation(LocationEntity locationEntity);
    List<EquipmentEntity> findAllByUsage(UsageEntity usageEntity);
    List<EquipmentEntity> findAllByInternalCodeContainingIgnoreCase(String internalCode);
    List<EquipmentEntity> findAllByEquipmentNameContainingIgnoreCase(String name);
    List<EquipmentEntity> findAllBySenaInventoryTagContainingIgnoreCase(String name);

    List<EquipmentEntity> findAllByMaintenanceDate(LocalDate currentDate);

    // COUNT ALLL
    long count();

    // creat dto de retorno pa esto

    // manintenance for this month
    @Query(value = "SELECT COUNT(*) FROM equipment e WHERE MONTH(e.maintenance_date) = :month", nativeQuery = true)
    long countByMaintenanceDateMonth(@Param("month") int month);

    long countByAvailableTrue();

    long countByAvailableFalse();

}
