package com.plazoleta.plazoleta.domain.spi;

import com.plazoleta.plazoleta.domain.model.PageResult;
import com.plazoleta.plazoleta.domain.model.Restaurant;

import java.util.Optional;

public interface RestaurantPersistencePort {
    Restaurant save(Restaurant restaurant);
    boolean existsByNit(String nit);
    Optional<Restaurant> findById(Long id);
    PageResult<Restaurant> findAllOrderedByName(int page, int size);
}
