package com.plazoleta.plazoleta.application.handler;

import com.plazoleta.plazoleta.domain.api.RestaurantServicePort;
import com.plazoleta.plazoleta.domain.model.PageResult;
import com.plazoleta.plazoleta.domain.model.Restaurant;
import com.plazoleta.plazoleta.infrastructure.input.rest.dto.PageResponseDto;
import com.plazoleta.plazoleta.infrastructure.input.rest.dto.RestaurantRequestDto;
import com.plazoleta.plazoleta.infrastructure.input.rest.dto.RestaurantResponseDto;
import com.plazoleta.plazoleta.infrastructure.input.rest.dto.RestaurantSummaryDto;
import org.springframework.stereotype.Service;

@Service
public class RestaurantHandler {

    private final RestaurantServicePort servicePort;

    public RestaurantHandler(RestaurantServicePort servicePort) {
        this.servicePort = servicePort;
    }

    public RestaurantResponseDto create(RestaurantRequestDto d) {
        Restaurant created = servicePort.createRestaurant(new Restaurant(null, d.name(), d.nit(),
                d.address(), d.phone(), d.logoUrl(), d.ownerId()));
        return new RestaurantResponseDto(created.id(), created.name(), created.nit(), created.address(),
                created.phone(), created.logoUrl(), created.ownerId());
    }

    public PageResponseDto<RestaurantSummaryDto> list(int page, int size) {
        PageResult<Restaurant> result = servicePort.listRestaurants(page, size);
        return new PageResponseDto<>(
                result.content().stream()
                        .map(r -> new RestaurantSummaryDto(r.name(), r.logoUrl()))
                        .toList(),
                result.page(), result.size(), result.totalElements(), result.totalPages());
    }
}
