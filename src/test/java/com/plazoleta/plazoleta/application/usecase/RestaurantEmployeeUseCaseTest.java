package com.plazoleta.plazoleta.application.usecase;

import com.plazoleta.plazoleta.domain.exception.DomainException;
import com.plazoleta.plazoleta.domain.exception.ForbiddenException;
import com.plazoleta.plazoleta.domain.exception.RestaurantNotFoundException;
import com.plazoleta.plazoleta.domain.model.Restaurant;
import com.plazoleta.plazoleta.domain.model.RestaurantEmployee;
import com.plazoleta.plazoleta.domain.spi.RestaurantEmployeePersistencePort;
import com.plazoleta.plazoleta.domain.spi.RestaurantPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantEmployeeUseCaseTest {

    private static final Long OWNER = 5L;
    private static final Long OTHER = 8L;

    @Mock
    RestaurantPersistencePort restaurantPersistence;
    @Mock
    RestaurantEmployeePersistencePort employeePersistence;
    @InjectMocks
    RestaurantEmployeeUseCase useCase;

    private Restaurant restaurant() {
        return new Restaurant(1L, "Sabor Costeño", "900123456", "Calle 1", "+573005698325",
                "http://logo.com/l.png", OWNER);
    }

    @Test
    void addEmployee_ok() {
        when(restaurantPersistence.findById(1L)).thenReturn(Optional.of(restaurant()));
        when(employeePersistence.findByEmployeeId(10L)).thenReturn(Optional.empty());
        when(employeePersistence.save(any(RestaurantEmployee.class)))
                .thenAnswer(i -> {
                    RestaurantEmployee l = i.getArgument(0);
                    return new RestaurantEmployee(99L, l.restaurantId(), l.employeeId());
                });

        RestaurantEmployee link = useCase.addEmployee(1L, 10L, OWNER);

        assertEquals(1L, link.restaurantId());
        assertEquals(10L, link.employeeId());
    }

    @Test
    void addEmployee_restauranteNoExiste() {
        when(restaurantPersistence.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class, () -> useCase.addEmployee(1L, 10L, OWNER));
        verify(employeePersistence, never()).save(any());
    }

    @Test
    void addEmployee_noEsPropietarioDelRestaurante() {
        when(restaurantPersistence.findById(1L)).thenReturn(Optional.of(restaurant()));

        assertThrows(ForbiddenException.class, () -> useCase.addEmployee(1L, 10L, OTHER));
        verify(employeePersistence, never()).save(any());
    }

    @Test
    void addEmployee_yaVinculadoAlMismoRestaurante_noDuplica() {
        RestaurantEmployee existing = new RestaurantEmployee(99L, 1L, 10L);
        when(restaurantPersistence.findById(1L)).thenReturn(Optional.of(restaurant()));
        when(employeePersistence.findByEmployeeId(10L)).thenReturn(Optional.of(existing));

        assertEquals(99L, useCase.addEmployee(1L, 10L, OWNER).id());
        verify(employeePersistence, never()).save(any());
    }

    @Test
    void addEmployee_yaPerteneceAOtroRestaurante() {
        RestaurantEmployee existing = new RestaurantEmployee(99L, 2L, 10L);
        when(restaurantPersistence.findById(1L)).thenReturn(Optional.of(restaurant()));
        when(employeePersistence.findByEmployeeId(10L)).thenReturn(Optional.of(existing));

        assertThrows(DomainException.class, () -> useCase.addEmployee(1L, 10L, OWNER));
        verify(employeePersistence, never()).save(any());
    }

    @Test
    void addEmployee_sinIdDeEmpleado() {
        assertThrows(DomainException.class, () -> useCase.addEmployee(1L, null, OWNER));
    }
}
