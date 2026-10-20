package com.plazoleta.plazoleta.infrastructure.input.rest.dto;

public record RestaurantResponseDto(Long id, String name, String nit, String address,
                                    String phone, String logoUrl, Long ownerId) {
}
