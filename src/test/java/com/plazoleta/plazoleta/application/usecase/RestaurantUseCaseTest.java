package com.plazoleta.plazoleta.application.usecase;

import com.plazoleta.plazoleta.domain.exception.DomainException;
import com.plazoleta.plazoleta.domain.model.PageResult;
import com.plazoleta.plazoleta.domain.model.Restaurant;
import com.plazoleta.plazoleta.domain.spi.RestaurantPersistencePort;
import com.plazoleta.plazoleta.domain.spi.UserClientPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    RestaurantPersistencePort persistence;
    @Mock
    UserClientPort userClient;
    @InjectMocks
    RestaurantUseCase useCase;

    private Restaurant restaurant(String name, String nit, String phone) {
        return new Restaurant(null, name, nit, "Calle 1 # 2-3", phone, "http://logo.com/l.png", 5L);
    }

    private Restaurant valid() {
        return restaurant("Sabor Costeño", "900123456", "+573005698325");
    }

    @Test
    void createRestaurant_ok() {
        when(userClient.findRole(5L)).thenReturn(Optional.of("OWNER"));
        when(persistence.existsByNit("900123456")).thenReturn(false);
        when(persistence.save(any(Restaurant.class))).thenAnswer(i -> i.getArgument(0));

        Restaurant saved = useCase.createRestaurant(valid());

        assertEquals("Sabor Costeño", saved.name());
        assertEquals(5L, saved.ownerId());
    }

    @Test
    void createRestaurant_usuarioNoEsPropietario() {
        when(userClient.findRole(5L)).thenReturn(Optional.of("CLIENT"));

        assertThrows(DomainException.class, () -> useCase.createRestaurant(valid()));
        verify(persistence, never()).save(any());
    }

    @Test
    void createRestaurant_usuarioNoExiste() {
        when(userClient.findRole(5L)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> useCase.createRestaurant(valid()));
        verify(persistence, never()).save(any());
    }

    @Test
    void createRestaurant_nitConLetras() {
        Restaurant r = restaurant("Sabor Costeño", "900abc", "+573005698325");
        assertThrows(DomainException.class, () -> useCase.createRestaurant(r));
    }

    @Test
    void createRestaurant_telefonoMuyLargo() {
        Restaurant r = restaurant("Sabor Costeño", "900123456", "+5730056983251234");
        assertThrows(DomainException.class, () -> useCase.createRestaurant(r));
    }

    @Test
    void createRestaurant_nombreSoloNumeros() {
        Restaurant r = restaurant("12345", "900123456", "+573005698325");
        assertThrows(DomainException.class, () -> useCase.createRestaurant(r));
    }

    @Test
    void createRestaurant_nitRepetido() {
        when(userClient.findRole(5L)).thenReturn(Optional.of("OWNER"));
        when(persistence.existsByNit("900123456")).thenReturn(true);

        assertThrows(DomainException.class, () -> useCase.createRestaurant(valid()));
        verify(persistence, never()).save(any());
    }

    // ---------- HU9: listar restaurantes ----------

    @Test
    void listRestaurants_ok() {
        Restaurant a = restaurant("Alfa Café", "900111111", "+573005698325");
        Restaurant b = restaurant("Beta Sushi", "900222222", "+573005698325");
        PageResult<Restaurant> page = new PageResult<>(List.of(a, b), 0, 10, 2, 1);
        when(persistence.findAllOrderedByName(0, 10)).thenReturn(page);

        PageResult<Restaurant> result = useCase.listRestaurants(0, 10);

        assertEquals(2, result.content().size());
        assertEquals("Alfa Café", result.content().get(0).name());
        assertEquals(1, result.totalPages());
    }

    @Test
    void listRestaurants_paginaNegativa() {
        assertThrows(DomainException.class, () -> useCase.listRestaurants(-1, 10));
    }

    @Test
    void listRestaurants_tamanoCero() {
        assertThrows(DomainException.class, () -> useCase.listRestaurants(0, 0));
    }

    @Test
    void listRestaurants_tamanoMuyGrande() {
        assertThrows(DomainException.class, () -> useCase.listRestaurants(0, 101));
    }
}
