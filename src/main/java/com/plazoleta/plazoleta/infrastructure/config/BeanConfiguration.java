package com.plazoleta.plazoleta.infrastructure.config;

import com.plazoleta.plazoleta.application.usecase.DishUseCase;
import com.plazoleta.plazoleta.application.usecase.RestaurantEmployeeUseCase;
import com.plazoleta.plazoleta.application.usecase.RestaurantUseCase;
import com.plazoleta.plazoleta.domain.api.DishServicePort;
import com.plazoleta.plazoleta.domain.spi.DishPersistencePort;
import com.plazoleta.plazoleta.domain.api.RestaurantEmployeeServicePort;
import com.plazoleta.plazoleta.domain.api.RestaurantServicePort;
import com.plazoleta.plazoleta.domain.spi.RestaurantEmployeePersistencePort;
import com.plazoleta.plazoleta.domain.spi.RestaurantPersistencePort;
import com.plazoleta.plazoleta.domain.spi.UserClientPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    RestaurantServicePort restaurantServicePort(RestaurantPersistencePort persistence,
                                                UserClientPort userClient) {
        return new RestaurantUseCase(persistence, userClient);
    }

    @Bean
    DishServicePort dishServicePort(DishPersistencePort dishPersistence,
                                    RestaurantPersistencePort restaurantPersistence) {
        return new DishUseCase(dishPersistence, restaurantPersistence);
    }

    @Bean
    RestaurantEmployeeServicePort restaurantEmployeeServicePort(RestaurantPersistencePort restaurantPersistence,
                                                                RestaurantEmployeePersistencePort employeePersistence) {
        return new RestaurantEmployeeUseCase(restaurantPersistence, employeePersistence);
    }
}
