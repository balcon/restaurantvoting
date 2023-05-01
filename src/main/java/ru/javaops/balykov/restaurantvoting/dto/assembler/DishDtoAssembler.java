package ru.javaops.balykov.restaurantvoting.dto.assembler;

import org.springframework.stereotype.Component;
import ru.javaops.balykov.restaurantvoting.dto.DishDto;
import ru.javaops.balykov.restaurantvoting.model.Dish;
import ru.javaops.balykov.restaurantvoting.web.rest.admin.DishController;

@Component
public class DishDtoAssembler extends BaseDtoAssembler<Dish, DishDto> {

    protected DishDtoAssembler() {
        super(DishController.class);
    }

    @Override
    protected DishDto of(Dish d) {
        return new DishDto(d.getName(), d.getPrice(), d.getOfferDate());
    }
}