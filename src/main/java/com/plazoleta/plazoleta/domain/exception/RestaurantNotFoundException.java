package com.plazoleta.plazoleta.domain.exception;

public class RestaurantNotFoundException extends DomainException {
    public RestaurantNotFoundException(Long id) {
        super("No existe un restaurante con id " + id);
    }
}
