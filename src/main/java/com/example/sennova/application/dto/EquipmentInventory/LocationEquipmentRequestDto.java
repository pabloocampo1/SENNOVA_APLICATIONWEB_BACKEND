package com.example.sennova.application.dto.EquipmentInventory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LocationEquipmentRequestDto(

        Long equipmentLocationId,

        @NotBlank(message = "El nombre de la ubicación no puede estar vacío")
        @Size(max = 255, message = "El nombre de la ubicación no puede tener más de 255 caracteres")
        String locationName
) {
}
