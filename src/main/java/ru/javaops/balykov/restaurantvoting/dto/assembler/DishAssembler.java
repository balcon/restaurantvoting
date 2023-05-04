package ru.javaops.balykov.restaurantvoting.dto.assembler;

import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;
import ru.javaops.balykov.restaurantvoting.dto.DishDto;
import ru.javaops.balykov.restaurantvoting.model.Dish;
import ru.javaops.balykov.restaurantvoting.web.rest.admin.DishController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DishAssembler extends BaseAssembler<Dish, DishDto> {

    protected DishAssembler() {
        super(DishController.class);
    }

    @Override
    public DishDto of(Dish d) {
        return new DishDto(d.getName(), d.getPrice(), d.getOfferDate());
    }

    public CollectionModel<DishDto> toCollectionModel(List<Dish> dishes, int restaurantId) {
        CollectionModel<DishDto> dtos = super.toCollectionModel(dishes);
        dtos.add(linkTo(methodOn(DishController.class).getAllOfRestaurant(restaurantId, null, Sort.unsorted()))
                .withRel("restaurant-dishes"));
        return dtos;
    }
}