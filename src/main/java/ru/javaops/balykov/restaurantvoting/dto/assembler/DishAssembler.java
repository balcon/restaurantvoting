package ru.javaops.balykov.restaurantvoting.dto.assembler;

import org.springframework.stereotype.Component;
import ru.javaops.balykov.restaurantvoting.dto.DishDto;
import ru.javaops.balykov.restaurantvoting.model.Dish;
import ru.javaops.balykov.restaurantvoting.web.rest.admin.DishController;

@Component
public class DishAssembler extends BaseAssembler<Dish, DishDto> {

    protected DishAssembler() {
        super(DishController.class);
    }

    @Override
    public DishDto of(Dish d) {
        return new DishDto(d.getName(), d.getPrice(), d.getOfferDate());
    }
}