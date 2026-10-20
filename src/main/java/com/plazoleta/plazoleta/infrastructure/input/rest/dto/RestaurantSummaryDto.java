package com.plazoleta.plazoleta.infrastructure.input.rest.dto;

/** Lo que ve el cliente en la lista de restaurantes: solo nombre y logo. */
public record RestaurantSummaryDto(String name, String logoUrl) {
}
