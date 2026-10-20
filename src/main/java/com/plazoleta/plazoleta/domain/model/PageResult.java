package com.plazoleta.plazoleta.domain.model;

import java.util.List;

/** Resultado paginado, independiente de Spring. */
public record PageResult<T>(List<T> content, int page, int size, long totalElements, int totalPages) {
}
