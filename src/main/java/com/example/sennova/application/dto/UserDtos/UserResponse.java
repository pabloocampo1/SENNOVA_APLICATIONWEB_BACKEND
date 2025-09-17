package com.example.sennova.application.dto.UserDtos;

import java.time.LocalDate;


public record UserResponse(
        Long userId,
        String name,
        Long dni,
        boolean available,
        Long phoneNumber,
        String email,
        String imageProfile
) {}
