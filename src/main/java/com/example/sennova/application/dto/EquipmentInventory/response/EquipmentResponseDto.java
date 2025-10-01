package com.example.sennova.application.dto.EquipmentInventory.response;


import java.time.LocalDate;
import java.time.LocalDateTime;

public record EquipmentResponseDto(
        Long equipmentId,
        String internalCode,
        String equipmentName,
        String brand,
        String model,
        Long serialNumber,
        LocalDate acquisitionDate,
        LocalDate maintenanceDate,
         String senaInventoryTag,
        String amperage,
        String voltage,
        double equipmentCost,
        String state,
        Boolean available,
        Long responsibleId,
        String responsibleName,
        Long locationId,
        String locationName,
        Long usageId,
        String usageName,
        LocalDateTime createAt,
        LocalDateTime updateAt,
        String imageUrl,
        String description
) {
}
