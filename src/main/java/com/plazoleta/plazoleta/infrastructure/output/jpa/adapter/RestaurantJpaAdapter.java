package com.plazoleta.plazoleta.infrastructure.output.jpa.adapter;

import com.plazoleta.plazoleta.domain.model.PageResult;
import com.plazoleta.plazoleta.domain.model.Restaurant;
import com.plazoleta.plazoleta.domain.spi.RestaurantPersistencePort;
import com.plazoleta.plazoleta.infrastructure.output.jpa.entity.RestaurantEntity;
import com.plazoleta.plazoleta.infrastructure.output.jpa.repository.RestaurantRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RestaurantJpaAdapter implements RestaurantPersistencePort {

    private final RestaurantRepository repository;

    public RestaurantJpaAdapter(RestaurantRepository repository) {
        this.repository = repository;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        return toDomain(repository.save(toEntity(restaurant)));
    }

    @Override
    public boolean existsByNit(String nit) {
        return repository.existsByNit(nit);
    }

    @Override
    public Optional<Restaurant> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public PageResult<Restaurant> findAllOrderedByName(int page, int size) {
        Page<RestaurantEntity> result = repository.findAll(
                PageRequest.of(page, size, Sort.by(Sort.Order.asc("name").ignoreCase())));
        return new PageResult<>(
                result.getContent().stream().map(this::toDomain).toList(),
                result.getNumber(), result.getSize(), result.getTotalElements(), result.getTotalPages());
    }

    private RestaurantEntity toEntity(Restaurant r) {
        RestaurantEntity e = new RestaurantEntity();
        e.setId(r.id());
        e.setName(r.name());
        e.setNit(r.nit());
        e.setAddress(r.address());
        e.setPhone(r.phone());
        e.setLogoUrl(r.logoUrl());
        e.setOwnerId(r.ownerId());
        return e;
    }

    private Restaurant toDomain(RestaurantEntity e) {
        return new Restaurant(e.getId(), e.getName(), e.getNit(), e.getAddress(),
                e.getPhone(), e.getLogoUrl(), e.getOwnerId());
    }
}
