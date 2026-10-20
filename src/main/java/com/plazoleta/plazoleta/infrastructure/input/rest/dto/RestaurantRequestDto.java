package com.plazoleta.plazoleta.infrastructure.input.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RestaurantRequestDto(
        @NotBlank(message = "El nombre es obligatorio") String name,
        @NotBlank(message = "El NIT es obligatorio") String nit,
        @NotBlank(message = "La dirección es obligatoria") String address,
        @NotBlank(message = "El teléfono es obligatorio") String phone,
        @NotBlank(message = "La URL del logo es obligatoria") String logoUrl,
        @NotNull(message = "El id del propietario es obligatorio") Long ownerId) {
}
