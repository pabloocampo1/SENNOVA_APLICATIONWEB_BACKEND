package com.example.sennova.application.dto.EquipmentInventory;

import java.time.LocalDate;

public record LocationEquipmentResponseDto(
        Long equipmentLocationId,
        String locationName,
        LocalDate createAt,
        LocalDate updateAt

) {
}
