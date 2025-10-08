package com.example.sennova.application.dto.inventory.ReagentInventory;


import java.time.LocalDate;

public record ReagentResponseDto(
        Long reagentsId,
        String reagentName,
        String brand,
        String purity,
        Integer units,
        Integer quantity,
        String unitOfMeasure,
        String imageUrl,
        String batch,
        LocalDate expirationDate,
        LocalDate createAt,
        LocalDate updateAt,
        String createdBy
) {}
