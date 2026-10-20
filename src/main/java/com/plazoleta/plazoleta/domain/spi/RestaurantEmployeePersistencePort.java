package com.plazoleta.plazoleta.domain.spi;

import com.plazoleta.plazoleta.domain.model.RestaurantEmployee;

import java.util.Optional;

public interface RestaurantEmployeePersistencePort {
    RestaurantEmployee save(RestaurantEmployee link);
    Optional<RestaurantEmployee> findByEmployeeId(Long employeeId);
}
