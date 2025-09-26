package com.example.sennova.application.dto.EquipmentInventory.response;

import java.time.LocalDate;

public record LocationEquipmentResponseDto(
        Long equipmentLocationId,
        String locationName,
        LocalDate createAt,
        LocalDate updateAt

) {
}
