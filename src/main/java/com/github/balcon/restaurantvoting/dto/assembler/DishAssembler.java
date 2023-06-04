package com.github.balcon.restaurantvoting.dto.assembler;

import com.github.balcon.restaurantvoting.dto.DishDto;
import com.github.balcon.restaurantvoting.model.Dish;
import com.github.balcon.restaurantvoting.web.rest.admin.DishController;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DishAssembler implements RepresentationModelAssembler<Dish, DishDto> {
    @Override
    public DishDto toModel(Dish d) {
        DishDto dto = new DishDto(d.getName(), d.getPrice(), d.getOfferDate());
        dto.add(linkTo(methodOn(DishController.class).getById(d.id())).withSelfRel());
        return dto;
    }

    public CollectionModel<DishDto> toCollectionModel(Iterable<? extends Dish> dishes, int restaurantId) {
        CollectionModel<DishDto> dtos = RepresentationModelAssembler.super.toCollectionModel(dishes);
        dtos.add(linkTo(methodOn(DishController.class)
                .getAllOfRestaurant(restaurantId, null, Sort.unsorted())).withSelfRel());
        return dtos;
    }
}