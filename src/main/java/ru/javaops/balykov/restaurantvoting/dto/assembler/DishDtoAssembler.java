package ru.javaops.balykov.restaurantvoting.dto.assembler;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import ru.javaops.balykov.restaurantvoting.dto.DishDto;
import ru.javaops.balykov.restaurantvoting.model.Dish;
import ru.javaops.balykov.restaurantvoting.web.rest.admin.DishController;

import java.util.ArrayList;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DishDtoAssembler implements RepresentationModelAssembler<Dish, DishDto> {

    @Override
    public DishDto toModel(Dish Dish) {
        DishDto model = toDto(Dish);
        model.add(linkTo(methodOn(DishController.class).getAll(Pageable.unpaged())).withRel("collection"));
        return model;
    }

    @Override
    public CollectionModel<DishDto> toCollectionModel(Iterable<? extends Dish> entities) {
        ArrayList<DishDto> DishDtos = new ArrayList<>();
        entities.forEach(e -> DishDtos.add(toDto(e)));
        CollectionModel<DishDto> models = CollectionModel.of(DishDtos);
        models.add(linkTo(methodOn(DishController.class).getAll(Pageable.unpaged())).withSelfRel());
        return models;
    }

    private DishDto toDto(Dish Dish) {
        DishDto model = new DishDto(Dish.getName(), Dish.getPrice(), Dish.getOfferDate());
        model.add(linkTo(methodOn(DishController.class).getById(Objects.requireNonNull(Dish.getId()))).withSelfRel());
        return model;
    }
}