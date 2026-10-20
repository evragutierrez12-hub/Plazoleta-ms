package com.plazoleta.plazoleta.infrastructure.input.rest;

import com.plazoleta.plazoleta.application.handler.DishHandler;
import com.plazoleta.plazoleta.infrastructure.input.rest.dto.DishRequestDto;
import com.plazoleta.plazoleta.infrastructure.input.rest.dto.DishResponseDto;
import com.plazoleta.plazoleta.infrastructure.input.rest.dto.DishStatusRequestDto;
import com.plazoleta.plazoleta.infrastructure.input.rest.dto.DishUpdateRequestDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dishes")
public class DishController {

    private final DishHandler handler;

    public DishController(DishHandler handler) {
        this.handler = handler;
    }

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping
    public ResponseEntity<DishResponseDto> create(@Valid @RequestBody DishRequestDto dto,
                                                  Authentication authentication) {
        Long callerId = Long.valueOf(authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(handler.create(dto, callerId));
    }

    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("/{id}")
    public ResponseEntity<DishResponseDto> update(@PathVariable Long id,
                                                  @Valid @RequestBody DishUpdateRequestDto dto,
                                                  Authentication authentication) {
        Long callerId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok(handler.update(id, dto, callerId));
    }

    @PreAuthorize("hasRole('OWNER')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<DishResponseDto> changeStatus(@PathVariable Long id,
                                                        @Valid @RequestBody DishStatusRequestDto dto,
                                                        Authentication authentication) {
        Long callerId = Long.valueOf(authentication.getName());
        return ResponseEntity.ok(handler.changeStatus(id, dto, callerId));
    }
}
