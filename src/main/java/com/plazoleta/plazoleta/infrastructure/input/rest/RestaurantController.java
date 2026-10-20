package com.plazoleta.plazoleta.infrastructure.input.rest;

import com.plazoleta.plazoleta.application.handler.RestaurantHandler;
import com.plazoleta.plazoleta.infrastructure.input.rest.dto.PageResponseDto;
import com.plazoleta.plazoleta.infrastructure.input.rest.dto.RestaurantRequestDto;
import com.plazoleta.plazoleta.infrastructure.input.rest.dto.RestaurantResponseDto;
import com.plazoleta.plazoleta.infrastructure.input.rest.dto.RestaurantSummaryDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/restaurants")
public class RestaurantController {

    private final RestaurantHandler handler;

    public RestaurantController(RestaurantHandler handler) {
        this.handler = handler;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<RestaurantResponseDto> create(@Valid @RequestBody RestaurantRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(handler.create(dto));
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping
    public ResponseEntity<PageResponseDto<RestaurantSummaryDto>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(handler.list(page, size));
    }
}
