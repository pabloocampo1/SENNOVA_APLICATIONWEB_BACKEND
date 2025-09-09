package com.example.sennova.application.dto.authDto;

import java.time.LocalDate;

public record LoginResponseDto(
        String accessToken,
//        String refreshToken,
        Long userId,
        Boolean status,
        String message,
        LocalDate timestamp,
        String authorities
) {
}
