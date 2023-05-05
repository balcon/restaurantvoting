package ru.javaops.balykov.restaurantvoting.util.validation;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;
import ru.javaops.balykov.restaurantvoting.repository.RestaurantRepository;

@Component
@RequiredArgsConstructor
public class RestaurantUniqueValidator implements Validator {

    private final RestaurantRepository repository;
    private final HttpServletRequest request;

    @Override
    public boolean supports(Class<?> clazz) {
        return Restaurant.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Restaurant restaurant = (Restaurant) target;
        repository.findByNameIgnoreCaseAndAddressIgnoreCase(restaurant.getName(), restaurant.getAddress())
                .ifPresent(r -> {
                    if (request.getMethod().equals("PUT") && r.id().equals(restaurant.id())) {
                        return;
                    }
                    errors.rejectValue("name", "", "Name and address must be unique");
                    errors.rejectValue("address", "", "Name and address must be unique");
                });
    }
}