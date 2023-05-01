package ru.javaops.balykov.restaurantvoting.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@RequiredArgsConstructor
@Getter
@Relation(collectionRelation = RestaurantDto.COLLECTION)
public class RestaurantDto extends RepresentationModel<RestaurantDto> {
    public static final String COLLECTION = "Restaurants";

    private final String name;
    private final String address;
}