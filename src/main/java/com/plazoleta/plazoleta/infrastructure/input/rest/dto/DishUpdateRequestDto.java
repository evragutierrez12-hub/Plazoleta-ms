package com.plazoleta.plazoleta.infrastructure.input.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** Solo se puede modificar el precio y la descripción del plato. */
public record DishUpdateRequestDto(
        @NotNull(message = "El precio es obligatorio") Integer price,
        @NotBlank(message = "La descripción es obligatoria") String description) {
}
