package com.plazoleta.plazoleta.domain.model;

public record Restaurant(Long id, String name, String nit, String address,
                         String phone, String logoUrl, Long ownerId) {
}
