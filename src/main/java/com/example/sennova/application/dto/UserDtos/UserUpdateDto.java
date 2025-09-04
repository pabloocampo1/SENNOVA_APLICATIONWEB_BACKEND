package com.example.sennova.application.dto.UserDtos;

import com.example.sennova.domain.model.RoleModel;

import jakarta.validation.constraints.*;

public record UserUpdateDto(

        @NotNull(message = "El id del usuario es obligatorio")
        Long userId,

        @NotBlank(message = "El nombre no puede estar vacío")
        @Size(max = 100, message = "El nombre no debe exceder 100 caracteres")
        String name,

        @NotNull(message = "El DNI es obligatorio")
        @Digits(integer = 10, fraction = 0, message = "El DNI debe ser numérico y tener hasta 10 dígitos")
        Long dni,

        boolean available,

        @NotNull(message = "El número de teléfono es obligatorio")
        @Digits(integer = 10, fraction = 0, message = "El teléfono debe ser numérico y tener hasta 10 dígitos")
        Long phoneNumber,

        @NotBlank(message = "El correo no puede estar vacío")
        @Email(message = "El correo debe tener un formato válido")
        String email,

        @Size(max = 255, message = "La URL de la imagen no debe exceder 255 caracteres")
        String imageProfile,

        @Size(max = 100, message = "El cargo no debe exceder 100 caracteres")
        String position,

        @NotNull(message = "El rol es obligatorio")
        RoleModel role
) {}

