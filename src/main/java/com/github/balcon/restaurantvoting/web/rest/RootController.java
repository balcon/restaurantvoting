package com.github.balcon.restaurantvoting.web.rest;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.balcon.restaurantvoting.config.AppConfig;
import com.github.balcon.restaurantvoting.dto.DishDto;
import com.github.balcon.restaurantvoting.dto.RestaurantDto;
import com.github.balcon.restaurantvoting.dto.UserDto;
import com.github.balcon.restaurantvoting.web.rest.admin.DishController;
import com.github.balcon.restaurantvoting.web.rest.admin.RestaurantController;
import com.github.balcon.restaurantvoting.web.rest.admin.UserController;
import com.github.balcon.restaurantvoting.web.rest.user.ProfileController;
import com.github.balcon.restaurantvoting.web.rest.user.RestaurantUserController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Hidden
@RestController
@RequestMapping(AppConfig.API_URL)
public class RootController {
    @GetMapping
    public RepresentationModel<?> get() {
        RepresentationModel<?> root = new RepresentationModel<>();
        root.add(linkTo(methodOn(UserController.class).getAll(null)).withRel(UserDto.COLLECTION));
        root.add(linkTo(methodOn(RestaurantController.class).getAll(null)).withRel(RestaurantDto.COLLECTION));
        root.add(linkTo(methodOn(DishController.class).getAll(null)).withRel(DishDto.COLLECTION));
        root.add(linkTo(methodOn(ProfileController.class).getAuth(null)).withRel("profile"));
        root.add(linkTo(methodOn(RestaurantUserController.class).getAll(null)).withRel("restaurants-with-dishes"));
        return root;
    }
}