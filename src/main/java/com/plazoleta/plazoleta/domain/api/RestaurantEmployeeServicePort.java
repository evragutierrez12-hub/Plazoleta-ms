package com.plazoleta.plazoleta.domain.api;

import com.plazoleta.plazoleta.domain.model.RestaurantEmployee;

public interface RestaurantEmployeeServicePort {
    /** callerId es el usuario autenticado; debe ser el propietario del restaurante. */
    RestaurantEmployee addEmployee(Long restaurantId, Long employeeId, Long callerId);
}
