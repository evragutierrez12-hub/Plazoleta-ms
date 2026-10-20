package com.plazoleta.plazoleta.application.usecase;

import com.plazoleta.plazoleta.domain.api.DishServicePort;
import com.plazoleta.plazoleta.domain.exception.DishNotFoundException;
import com.plazoleta.plazoleta.domain.exception.DomainException;
import com.plazoleta.plazoleta.domain.exception.ForbiddenException;
import com.plazoleta.plazoleta.domain.model.Dish;
import com.plazoleta.plazoleta.domain.model.Restaurant;
import com.plazoleta.plazoleta.domain.spi.DishPersistencePort;
import com.plazoleta.plazoleta.domain.spi.RestaurantPersistencePort;

import java.util.Objects;

public class DishUseCase implements DishServicePort {

    private final DishPersistencePort dishPersistence;
    private final RestaurantPersistencePort restaurantPersistence;

    public DishUseCase(DishPersistencePort dishPersistence, RestaurantPersistencePort restaurantPersistence) {
        this.dishPersistence = dishPersistence;
        this.restaurantPersistence = restaurantPersistence;
    }

    @Override
    public Dish createDish(Dish d, Long callerId) {
        validate(d);

        Restaurant restaurant = restaurantPersistence.findById(d.restaurantId())
                .orElseThrow(() -> new DomainException("No existe un restaurante con id " + d.restaurantId()));
        checkOwner(restaurant, callerId);

        // Todo plato recién creado queda activo
        return dishPersistence.save(new Dish(null, d.name(), d.price(), d.description(),
                d.imageUrl(), d.category(), true, d.restaurantId()));
    }

    @Override
    public Dish updateDish(Long id, Integer price, String description, Long callerId) {
        if (price == null) {
            throw new DomainException("El precio es obligatorio");
        }
        if (price <= 0) {
            throw new DomainException("El precio debe ser un número entero mayor a 0");
        }
        require(description, "La descripción es obligatoria");

        Dish current = dishPersistence.findById(id).orElseThrow(() -> new DishNotFoundException(id));
        Restaurant restaurant = restaurantPersistence.findById(current.restaurantId())
                .orElseThrow(() -> new DomainException("No existe un restaurante con id " + current.restaurantId()));
        checkOwner(restaurant, callerId);

        // Solo cambian precio y descripción; el resto del plato se conserva igual
        return dishPersistence.save(new Dish(current.id(), current.name(), price, description,
                current.imageUrl(), current.category(), current.active(), current.restaurantId()));
    }

    private void checkOwner(Restaurant restaurant, Long callerId) {
        if (!Objects.equals(restaurant.ownerId(), callerId)) {
            throw new ForbiddenException("No puedes gestionar platos de un restaurante que no es tuyo");
        }
    }

    private void validate(Dish d) {
        require(d.name(), "El nombre del plato es obligatorio");
        require(d.description(), "La descripción es obligatoria");
        require(d.imageUrl(), "La URL de la imagen es obligatoria");
        require(d.category(), "La categoría es obligatoria");
        if (d.price() == null) {
            throw new DomainException("El precio es obligatorio");
        }
        if (d.price() <= 0) {
            throw new DomainException("El precio debe ser un número entero mayor a 0");
        }
        if (d.restaurantId() == null) {
            throw new DomainException("El id del restaurante es obligatorio");
        }
    }

    private void require(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new DomainException(message);
        }
    }

    @Override
    public Dish changeDishStatus(Long id, Boolean active, Long callerId) {
        if (active == null) {
            throw new DomainException("El campo active es obligatorio (true o false)");
        }

        Dish current = dishPersistence.findById(id).orElseThrow(() -> new DishNotFoundException(id));
        Restaurant restaurant = restaurantPersistence.findById(current.restaurantId())
                .orElseThrow(() -> new DomainException("No existe un restaurante con id " + current.restaurantId()));
        checkOwner(restaurant, callerId);

        // Solo cambia el estado; el resto del plato se conserva igual
        return dishPersistence.save(new Dish(current.id(), current.name(), current.price(), current.description(),
                current.imageUrl(), current.category(), active, current.restaurantId()));
    }
}
