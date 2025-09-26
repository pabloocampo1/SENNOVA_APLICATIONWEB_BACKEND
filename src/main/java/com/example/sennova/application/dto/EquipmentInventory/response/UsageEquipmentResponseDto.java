package com.example.sennova.application.dto.EquipmentInventory.response;

import java.time.LocalDate;

public record UsageEquipmentResponseDto(
        Long equipmentUsageId,
        String usageName,
        LocalDate createAt,
        LocalDate updateAt
) {
}
