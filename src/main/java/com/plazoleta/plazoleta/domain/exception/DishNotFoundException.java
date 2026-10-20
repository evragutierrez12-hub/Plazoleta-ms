package com.plazoleta.plazoleta.domain.exception;

public class DishNotFoundException extends DomainException {
    public DishNotFoundException(Long id) {
        super("No existe un plato con id " + id);
    }
}
