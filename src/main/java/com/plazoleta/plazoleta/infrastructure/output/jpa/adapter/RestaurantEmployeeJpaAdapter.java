package com.plazoleta.plazoleta.infrastructure.output.jpa.adapter;

import com.plazoleta.plazoleta.domain.model.RestaurantEmployee;
import com.plazoleta.plazoleta.domain.spi.RestaurantEmployeePersistencePort;
import com.plazoleta.plazoleta.infrastructure.output.jpa.entity.RestaurantEmployeeEntity;
import com.plazoleta.plazoleta.infrastructure.output.jpa.repository.RestaurantEmployeeRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RestaurantEmployeeJpaAdapter implements RestaurantEmployeePersistencePort {

    private final RestaurantEmployeeRepository repository;

    public RestaurantEmployeeJpaAdapter(RestaurantEmployeeRepository repository) {
        this.repository = repository;
    }

    @Override
    public RestaurantEmployee save(RestaurantEmployee link) {
        RestaurantEmployeeEntity e = new RestaurantEmployeeEntity();
        e.setId(link.id());
        e.setRestaurantId(link.restaurantId());
        e.setEmployeeId(link.employeeId());
        return toDomain(repository.save(e));
    }

    @Override
    public Optional<RestaurantEmployee> findByEmployeeId(Long employeeId) {
        return repository.findByEmployeeId(employeeId).map(this::toDomain);
    }

    private RestaurantEmployee toDomain(RestaurantEmployeeEntity e) {
        return new RestaurantEmployee(e.getId(), e.getRestaurantId(), e.getEmployeeId());
    }
}
