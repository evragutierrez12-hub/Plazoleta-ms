package com.plazoleta.plazoleta.infrastructure.input.rest.dto;

public record DishResponseDto(Long id, String name, Integer price, String description,
                              String imageUrl, String category, Boolean active, Long restaurantId) {
}
