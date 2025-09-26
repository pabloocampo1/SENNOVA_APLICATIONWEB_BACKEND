package com.example.sennova.application.dto.EquipmentInventory.response;

public record EquipmentStatisticsSummaryCardResponse(
        long count,
        long countAvailableTrue,
        long countAvailableFalse,
        long countMaintenanceMonth
) {
}
