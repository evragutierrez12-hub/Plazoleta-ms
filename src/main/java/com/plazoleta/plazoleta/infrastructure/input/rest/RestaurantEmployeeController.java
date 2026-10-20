package com.plazoleta.plazoleta.infrastructure.input.rest;

import com.plazoleta.plazoleta.application.handler.RestaurantEmployeeHandler;
import com.plazoleta.plazoleta.infrastructure.input.rest.dto.EmployeeLinkRequestDto;
import com.plazoleta.plazoleta.infrastructure.input.rest.dto.RestaurantEmployeeResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/restaurants/{restaurantId}/employees")
public class RestaurantEmployeeController {

    private final RestaurantEmployeeHandler handler;

    public RestaurantEmployeeController(RestaurantEmployeeHandler handler) {
        this.handler = handler;
    }

    /** Lo llama usuarios-ms después de crear la cuenta de un empleado. */
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping
    public ResponseEntity<RestaurantEmployeeResponseDto> addEmployee(@PathVariable Long restaurantId,
                                                                     @Valid @RequestBody EmployeeLinkRequestDto dto,
                                                                     Authentication authentication) {
        Long callerId = Long.valueOf(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(handler.addEmployee(restaurantId, dto.employeeId(), callerId));
    }
}
