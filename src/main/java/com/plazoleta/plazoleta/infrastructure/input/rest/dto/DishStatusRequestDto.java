package com.plazoleta.plazoleta.infrastructure.input.rest.dto;

import jakarta.validation.constraints.NotNull;

/** Body para habilitar o deshabilitar un plato: {"active": true} o {"active": false}. */
public record DishStatusRequestDto(
        @NotNull(message = "El campo active es obligatorio (true o false)") Boolean active) {
}
