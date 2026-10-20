package com.plazoleta.plazoleta.domain.api;

import com.plazoleta.plazoleta.domain.model.Dish;

public interface DishServicePort {
    /** callerId es el id del usuario autenticado; debe ser el propietario del restaurante. */
    Dish createDish(Dish dish, Long callerId);
    Dish updateDish(Long id, Integer price, String description, Long callerId);
    /** Habilita (true) o deshabilita (false) un plato. Solo el propietario de su restaurante. */
    Dish changeDishStatus(Long id, Boolean active, Long callerId);
}
