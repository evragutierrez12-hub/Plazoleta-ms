package com.plazoleta.plazoleta.infrastructure.input.rest.dto;

import java.util.List;

public record PageResponseDto<T>(List<T> content, int page, int size, long totalElements, int totalPages) {
}
