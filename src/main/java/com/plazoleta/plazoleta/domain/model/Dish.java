package com.plazoleta.plazoleta.domain.model;

public record Dish(Long id, String name, Integer price, String description,
                   String imageUrl, String category, Boolean active, Long restaurantId) {
}
