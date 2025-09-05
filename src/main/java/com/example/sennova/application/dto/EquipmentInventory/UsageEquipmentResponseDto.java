package com.example.sennova.application.dto.EquipmentInventory;

import java.time.LocalDate;

public record UsageEquipmentResponseDto(
        Long equipmentUsageId,
        String usageName,
        LocalDate createAt,
        LocalDate updateAt
) {
}
