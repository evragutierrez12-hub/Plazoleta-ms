package com.plazoleta.plazoleta.application.usecase;

import com.plazoleta.plazoleta.domain.exception.DishNotFoundException;
import com.plazoleta.plazoleta.domain.exception.DomainException;
import com.plazoleta.plazoleta.domain.exception.ForbiddenException;
import com.plazoleta.plazoleta.domain.model.Dish;
import com.plazoleta.plazoleta.domain.model.Restaurant;
import com.plazoleta.plazoleta.domain.spi.DishPersistencePort;
import com.plazoleta.plazoleta.domain.spi.RestaurantPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DishUseCaseTest {

    private static final Long OWNER = 5L;
    private static final Long OTHER = 8L;

    @Mock
    DishPersistencePort dishPersistence;
    @Mock
    RestaurantPersistencePort restaurantPersistence;
    @InjectMocks
    DishUseCase useCase;

    private Restaurant restaurant() {
        return new Restaurant(1L, "Sabor Costeño", "900123456", "Calle 1", "+573005698325",
                "http://logo.com/l.png", OWNER);
    }

    private Dish dish(Integer price, String category) {
        return new Dish(null, "Bandeja paisa", price, "Plato típico", "http://img.com/p.png",
                category, null, 1L);
    }

    private Dish existing() {
        return new Dish(3L, "Bandeja paisa", 25000, "Plato típico", "http://img.com/p.png",
                "Almuerzos", true, 1L);
    }

    // ---------- crear ----------

    @Test
    void createDish_ok_quedaActivo() {
        when(restaurantPersistence.findById(1L)).thenReturn(Optional.of(restaurant()));
        when(dishPersistence.save(any(Dish.class))).thenAnswer(i -> i.getArgument(0));

        Dish saved = useCase.createDish(dish(25000, "Almuerzos"), OWNER);

        assertTrue(saved.active());
        assertEquals(25000, saved.price());
        assertEquals(1L, saved.restaurantId());
    }

    @Test
    void createDish_restauranteNoExiste() {
        when(restaurantPersistence.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> useCase.createDish(dish(25000, "Almuerzos"), OWNER));
        verify(dishPersistence, never()).save(any());
    }

    @Test
    void createDish_noEsPropietarioDelRestaurante() {
        when(restaurantPersistence.findById(1L)).thenReturn(Optional.of(restaurant()));

        assertThrows(ForbiddenException.class, () -> useCase.createDish(dish(25000, "Almuerzos"), OTHER));
        verify(dishPersistence, never()).save(any());
    }

    @Test
    void createDish_precioCero() {
        assertThrows(DomainException.class, () -> useCase.createDish(dish(0, "Almuerzos"), OWNER));
    }

    @Test
    void createDish_precioNegativo() {
        assertThrows(DomainException.class, () -> useCase.createDish(dish(-500, "Almuerzos"), OWNER));
    }

    @Test
    void createDish_precioNulo() {
        assertThrows(DomainException.class, () -> useCase.createDish(dish(null, "Almuerzos"), OWNER));
    }

    @Test
    void createDish_categoriaVacia() {
        assertThrows(DomainException.class, () -> useCase.createDish(dish(25000, " "), OWNER));
    }

    // ---------- modificar ----------

    @Test
    void updateDish_ok_soloCambiaPrecioYDescripcion() {
        when(dishPersistence.findById(3L)).thenReturn(Optional.of(existing()));
        when(restaurantPersistence.findById(1L)).thenReturn(Optional.of(restaurant()));
        when(dishPersistence.save(any(Dish.class))).thenAnswer(i -> i.getArgument(0));

        Dish updated = useCase.updateDish(3L, 30000, "Nueva descripción", OWNER);

        assertEquals(30000, updated.price());
        assertEquals("Nueva descripción", updated.description());
        assertEquals("Bandeja paisa", updated.name());
        assertEquals("Almuerzos", updated.category());
        assertTrue(updated.active());
        assertEquals(1L, updated.restaurantId());
    }

    @Test
    void updateDish_noExiste() {
        when(dishPersistence.findById(99L)).thenReturn(Optional.empty());

        assertThrows(DishNotFoundException.class, () -> useCase.updateDish(99L, 30000, "Desc", OWNER));
        verify(dishPersistence, never()).save(any());
    }

    @Test
    void updateDish_otroPropietarioNoPuede() {
        when(dishPersistence.findById(3L)).thenReturn(Optional.of(existing()));
        when(restaurantPersistence.findById(1L)).thenReturn(Optional.of(restaurant()));

        assertThrows(ForbiddenException.class, () -> useCase.updateDish(3L, 30000, "Desc", OTHER));
        verify(dishPersistence, never()).save(any());
    }

    @Test
    void updateDish_precioInvalido() {
        assertThrows(DomainException.class, () -> useCase.updateDish(3L, 0, "Desc", OWNER));
        assertThrows(DomainException.class, () -> useCase.updateDish(3L, -10, "Desc", OWNER));
        assertThrows(DomainException.class, () -> useCase.updateDish(3L, null, "Desc", OWNER));
    }

    @Test
    void updateDish_descripcionVacia() {
        assertThrows(DomainException.class, () -> useCase.updateDish(3L, 30000, " ", OWNER));
    }

    // ---------- habilitar / deshabilitar ----------

    @Test
    void changeDishStatus_deshabilitar_soloCambiaActive() {
        when(dishPersistence.findById(3L)).thenReturn(Optional.of(existing()));
        when(restaurantPersistence.findById(1L)).thenReturn(Optional.of(restaurant()));
        when(dishPersistence.save(any(Dish.class))).thenAnswer(i -> i.getArgument(0));

        Dish updated = useCase.changeDishStatus(3L, false, OWNER);

        assertFalse(updated.active());
        assertEquals("Bandeja paisa", updated.name());
        assertEquals(25000, updated.price());
        assertEquals(1L, updated.restaurantId());
    }

    @Test
    void changeDishStatus_habilitar() {
        Dish inactive = new Dish(3L, "Bandeja paisa", 25000, "Plato típico", "http://img.com/p.png",
                "Almuerzos", false, 1L);
        when(dishPersistence.findById(3L)).thenReturn(Optional.of(inactive));
        when(restaurantPersistence.findById(1L)).thenReturn(Optional.of(restaurant()));
        when(dishPersistence.save(any(Dish.class))).thenAnswer(i -> i.getArgument(0));

        assertTrue(useCase.changeDishStatus(3L, true, OWNER).active());
    }

    @Test
    void changeDishStatus_noExiste() {
        when(dishPersistence.findById(99L)).thenReturn(Optional.empty());

        assertThrows(DishNotFoundException.class, () -> useCase.changeDishStatus(99L, false, OWNER));
        verify(dishPersistence, never()).save(any());
    }

    @Test
    void changeDishStatus_platoDeOtroRestaurante() {
        when(dishPersistence.findById(3L)).thenReturn(Optional.of(existing()));
        when(restaurantPersistence.findById(1L)).thenReturn(Optional.of(restaurant()));

        assertThrows(ForbiddenException.class, () -> useCase.changeDishStatus(3L, false, OTHER));
        verify(dishPersistence, never()).save(any());
    }

    @Test
    void changeDishStatus_activeNulo() {
        assertThrows(DomainException.class, () -> useCase.changeDishStatus(3L, null, OWNER));
    }
}
