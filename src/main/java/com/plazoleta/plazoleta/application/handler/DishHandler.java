package com.plazoleta.plazoleta.application.handler;

import com.plazoleta.plazoleta.domain.api.DishServicePort;
import com.plazoleta.plazoleta.domain.model.Dish;
import com.plazoleta.plazoleta.infrastructure.input.rest.dto.DishRequestDto;
import com.plazoleta.plazoleta.infrastructure.input.rest.dto.DishResponseDto;
import com.plazoleta.plazoleta.infrastructure.input.rest.dto.DishStatusRequestDto;
import com.plazoleta.plazoleta.infrastructure.input.rest.dto.DishUpdateRequestDto;
import org.springframework.stereotype.Service;

@Service
public class DishHandler {

    private final DishServicePort servicePort;

    public DishHandler(DishServicePort servicePort) {
        this.servicePort = servicePort;
    }

    public DishResponseDto create(DishRequestDto d, Long callerId) {
        Dish created = servicePort.createDish(new Dish(null, d.name(), d.price(), d.description(),
                d.imageUrl(), d.category(), null, d.restaurantId()), callerId);
        return toResponse(created);
    }

    public DishResponseDto update(Long id, DishUpdateRequestDto d, Long callerId) {
        return toResponse(servicePort.updateDish(id, d.price(), d.description(), callerId));
    }

    public DishResponseDto changeStatus(Long id, DishStatusRequestDto d, Long callerId) {
        return toResponse(servicePort.changeDishStatus(id, d.active(), callerId));
    }

    private DishResponseDto toResponse(Dish d) {
        return new DishResponseDto(d.id(), d.name(), d.price(), d.description(),
                d.imageUrl(), d.category(), d.active(), d.restaurantId());
    }
}
