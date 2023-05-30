package com.github.balcon.restaurantvoting.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
@Relation(collectionRelation = RestaurantDto.COLLECTION)
public class RestaurantWithDishesDto extends RepresentationModel<RestaurantWithDishesDto> {
    public static final String COLLECTION = "restaurants";

    private final String name;
    private final String address;
    private int votes = 0;
    private final List<DishDto> dishes;
}