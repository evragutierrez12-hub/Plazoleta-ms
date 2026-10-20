package com.plazoleta.plazoleta.infrastructure.input.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DishRequestDto(
        @NotBlank(message = "El nombre del plato es obligatorio") String name,
        @NotNull(message = "El precio es obligatorio") Integer price,
        @NotBlank(message = "La descripción es obligatoria") String description,
        @NotBlank(message = "La URL de la imagen es obligatoria") String imageUrl,
        @NotBlank(message = "La categoría es obligatoria") String category,
        @NotNull(message = "El id del restaurante es obligatorio") Long restaurantId) {
}
