package com.example.sennova.application.dto.inventory.EquipmentInventory.response;

public record EquipmentStatisticsSummaryCardResponse(
        long count,
        long countAvailableTrue,
        long countAvailableFalse,
        long countMaintenanceMonth
) {
}
