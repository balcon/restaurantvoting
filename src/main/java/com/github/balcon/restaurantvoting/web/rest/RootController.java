package com.github.balcon.restaurantvoting.web.rest;

import com.github.balcon.restaurantvoting.config.AppConfig;
import com.github.balcon.restaurantvoting.dto.RestaurantDto;
import com.github.balcon.restaurantvoting.dto.UserDto;
import com.github.balcon.restaurantvoting.web.rest.admin.RestaurantController;
import com.github.balcon.restaurantvoting.web.rest.admin.UserController;
import com.github.balcon.restaurantvoting.web.rest.user.ProfileController;
import com.github.balcon.restaurantvoting.web.rest.user.RestaurantUserController;
import com.github.balcon.restaurantvoting.web.rest.user.VoteController;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

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
        root.add(linkTo(methodOn(ProfileController.class).getAuth(null)).withRel("profile"));
        root.add(linkTo(methodOn(RestaurantUserController.class).getAll()).withRel("restaurants-with-dishes"));
        root.add(linkTo(methodOn(VoteController.class).getToday(null, Optional.empty())).withRel("vote"));
        return root;
    }
}