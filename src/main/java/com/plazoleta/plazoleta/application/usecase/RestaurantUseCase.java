package com.plazoleta.plazoleta.application.usecase;

import com.plazoleta.plazoleta.domain.api.RestaurantServicePort;
import com.plazoleta.plazoleta.domain.exception.DomainException;
import com.plazoleta.plazoleta.domain.model.PageResult;
import com.plazoleta.plazoleta.domain.model.Restaurant;
import com.plazoleta.plazoleta.domain.spi.RestaurantPersistencePort;
import com.plazoleta.plazoleta.domain.spi.UserClientPort;

import java.util.regex.Pattern;

public class RestaurantUseCase implements RestaurantServicePort {

    private static final Pattern NUMERIC = Pattern.compile("^\\d+$");
    private static final int MAX_PAGE_SIZE = 100;
    private static final Pattern PHONE = Pattern.compile("^\\+?\\d+$");

    private final RestaurantPersistencePort persistence;
    private final UserClientPort userClient;

    public RestaurantUseCase(RestaurantPersistencePort persistence, UserClientPort userClient) {
        this.persistence = persistence;
        this.userClient = userClient;
    }

    @Override
    public Restaurant createRestaurant(Restaurant r) {
        validate(r);

        String role = userClient.findRole(r.ownerId())
                .orElseThrow(() -> new DomainException("No existe un usuario con id " + r.ownerId()));
        if (!"OWNER".equals(role)) {
            throw new DomainException("El usuario indicado no tiene rol de propietario");
        }
        if (persistence.existsByNit(r.nit())) {
            throw new DomainException("Ya existe un restaurante con ese NIT");
        }

        return persistence.save(new Restaurant(null, r.name(), r.nit(), r.address(),
                r.phone(), r.logoUrl(), r.ownerId()));
    }

    @Override
    public PageResult<Restaurant> listRestaurants(int page, int size) {
        if (page < 0) {
            throw new DomainException("La página no puede ser negativa");
        }
        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new DomainException("El tamaño de página debe estar entre 1 y " + MAX_PAGE_SIZE);
        }
        return persistence.findAllOrderedByName(page, size);
    }

    private void validate(Restaurant r) {
        require(r.name(), "El nombre es obligatorio");
        require(r.nit(), "El NIT es obligatorio");
        require(r.address(), "La dirección es obligatoria");
        require(r.phone(), "El teléfono es obligatorio");
        require(r.logoUrl(), "La URL del logo es obligatoria");
        if (r.ownerId() == null) {
            throw new DomainException("El id del propietario es obligatorio");
        }

        if (!NUMERIC.matcher(r.nit()).matches()) {
            throw new DomainException("El NIT debe ser únicamente numérico");
        }
        if (r.phone().length() > 13 || !PHONE.matcher(r.phone()).matches()) {
            throw new DomainException("El teléfono debe tener máximo 13 caracteres y solo puede incluir + al inicio");
        }
        if (NUMERIC.matcher(r.name().trim()).matches()) {
            throw new DomainException("El nombre no puede contener solo números");
        }
    }

    private void require(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new DomainException(message);
        }
    }
}
