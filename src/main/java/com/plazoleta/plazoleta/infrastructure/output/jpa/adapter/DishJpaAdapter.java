package com.plazoleta.plazoleta.infrastructure.output.jpa.adapter;

import com.plazoleta.plazoleta.domain.model.Dish;
import com.plazoleta.plazoleta.domain.spi.DishPersistencePort;
import com.plazoleta.plazoleta.infrastructure.output.jpa.entity.DishEntity;
import com.plazoleta.plazoleta.infrastructure.output.jpa.repository.DishRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DishJpaAdapter implements DishPersistencePort {

    private final DishRepository repository;

    public DishJpaAdapter(DishRepository repository) {
        this.repository = repository;
    }

    @Override
    public Dish save(Dish dish) {
        return toDomain(repository.save(toEntity(dish)));
    }

    @Override
    public Optional<Dish> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    private DishEntity toEntity(Dish d) {
        DishEntity e = new DishEntity();
        e.setId(d.id());
        e.setName(d.name());
        e.setPrice(d.price());
        e.setDescription(d.description());
        e.setImageUrl(d.imageUrl());
        e.setCategory(d.category());
        e.setActive(d.active());
        e.setRestaurantId(d.restaurantId());
        return e;
    }

    private Dish toDomain(DishEntity e) {
        return new Dish(e.getId(), e.getName(), e.getPrice(), e.getDescription(),
                e.getImageUrl(), e.getCategory(), e.getActive(), e.getRestaurantId());
    }
}
