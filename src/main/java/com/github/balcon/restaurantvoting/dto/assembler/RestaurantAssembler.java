package com.github.balcon.restaurantvoting.dto.assembler;

import com.github.balcon.restaurantvoting.dto.DishDto;
import com.github.balcon.restaurantvoting.dto.RestaurantDto;
import com.github.balcon.restaurantvoting.model.Restaurant;
import com.github.balcon.restaurantvoting.web.rest.admin.DishController;
import com.github.balcon.restaurantvoting.web.rest.admin.RestaurantController;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RestaurantAssembler extends BaseAssembler<Restaurant, RestaurantDto> {

    protected RestaurantAssembler() {
        super(RestaurantController.class);
    }

    @Override
    protected RestaurantDto of(Restaurant r) {
        return new RestaurantDto(r.getName(), r.getAddress());
    }

    @Override
    public RestaurantDto toModel(Restaurant restaurant) {
        RestaurantDto dto = super.toModel(restaurant);
        dto.add(linkTo(methodOn(DishController.class)
                .getAllOfRestaurant(restaurant.id(), null, null))
                .withRel(DishDto.COLLECTION));
        return dto;
    }
}