package com.plazoleta.plazoleta.application.usecase;

import com.plazoleta.plazoleta.domain.api.RestaurantEmployeeServicePort;
import com.plazoleta.plazoleta.domain.exception.DomainException;
import com.plazoleta.plazoleta.domain.exception.ForbiddenException;
import com.plazoleta.plazoleta.domain.exception.RestaurantNotFoundException;
import com.plazoleta.plazoleta.domain.model.Restaurant;
import com.plazoleta.plazoleta.domain.model.RestaurantEmployee;
import com.plazoleta.plazoleta.domain.spi.RestaurantEmployeePersistencePort;
import com.plazoleta.plazoleta.domain.spi.RestaurantPersistencePort;

import java.util.Objects;
import java.util.Optional;

public class RestaurantEmployeeUseCase implements RestaurantEmployeeServicePort {

    private final RestaurantPersistencePort restaurantPersistence;
    private final RestaurantEmployeePersistencePort employeePersistence;

    public RestaurantEmployeeUseCase(RestaurantPersistencePort restaurantPersistence,
                                     RestaurantEmployeePersistencePort employeePersistence) {
        this.restaurantPersistence = restaurantPersistence;
        this.employeePersistence = employeePersistence;
    }

    @Override
    public RestaurantEmployee addEmployee(Long restaurantId, Long employeeId, Long callerId) {
        if (employeeId == null) {
            throw new DomainException("El id del empleado es obligatorio");
        }

        Restaurant restaurant = restaurantPersistence.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));
        if (!Objects.equals(restaurant.ownerId(), callerId)) {
            throw new ForbiddenException("No puedes agregar empleados a un restaurante que no es tuyo");
        }

        Optional<RestaurantEmployee> existing = employeePersistence.findByEmployeeId(employeeId);
        if (existing.isPresent()) {
            if (Objects.equals(existing.get().restaurantId(), restaurantId)) {
                return existing.get();
            }
            throw new DomainException("El empleado ya pertenece a otro restaurante");
        }

        return employeePersistence.save(new RestaurantEmployee(null, restaurantId, employeeId));
    }
}
