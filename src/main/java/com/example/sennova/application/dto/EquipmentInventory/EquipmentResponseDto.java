package com.example.sennova.application.dto.EquipmentInventory;


import java.time.LocalDate;

public record EquipmentResponseDto(
        Long equipmentId,
        String internalCode,
        String equipmentName,
        String brand,
        String model,
        Long serialNumber,
        LocalDate acquisitionDate,
        LocalDate maintenanceDate,
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
        String usageName
) {
}
