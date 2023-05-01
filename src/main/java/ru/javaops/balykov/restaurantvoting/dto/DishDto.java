package ru.javaops.balykov.restaurantvoting.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

@RequiredArgsConstructor
@Relation(collectionRelation = DishDto.COLLECTION)
public class DishDto extends RepresentationModel<DishDto> {
    public static final String COLLECTION = "dishes";
    private final String name;
    private final int price;
    private final LocalDate offerDate;
}