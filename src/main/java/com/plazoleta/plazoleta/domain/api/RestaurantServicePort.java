package com.plazoleta.plazoleta.domain.api;

import com.plazoleta.plazoleta.domain.model.PageResult;
import com.plazoleta.plazoleta.domain.model.Restaurant;

public interface RestaurantServicePort {
    Restaurant createRestaurant(Restaurant restaurant);
    /** Lista los restaurantes en orden alfabético por nombre, paginados. */
    PageResult<Restaurant> listRestaurants(int page, int size);
}
