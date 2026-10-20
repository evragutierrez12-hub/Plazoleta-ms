package com.plazoleta.plazoleta.infrastructure.input.rest.dto;

import jakarta.validation.constraints.NotNull;

public record EmployeeLinkRequestDto(
        @NotNull(message = "El id del empleado es obligatorio") Long employeeId) {
}
