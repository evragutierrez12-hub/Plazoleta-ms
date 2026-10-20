package com.plazoleta.plazoleta.application.handler;

import com.plazoleta.plazoleta.domain.api.RestaurantEmployeeServicePort;
import com.plazoleta.plazoleta.domain.model.RestaurantEmployee;
import com.plazoleta.plazoleta.infrastructure.input.rest.dto.RestaurantEmployeeResponseDto;
import org.springframework.stereotype.Service;

@Service
public class RestaurantEmployeeHandler {

    private final RestaurantEmployeeServicePort servicePort;

    public RestaurantEmployeeHandler(RestaurantEmployeeServicePort servicePort) {
        this.servicePort = servicePort;
    }

    public RestaurantEmployeeResponseDto addEmployee(Long restaurantId, Long employeeId, Long callerId) {
        RestaurantEmployee link = servicePort.addEmployee(restaurantId, employeeId, callerId);
        return new RestaurantEmployeeResponseDto(link.id(), link.restaurantId(), link.employeeId());
    }
}
