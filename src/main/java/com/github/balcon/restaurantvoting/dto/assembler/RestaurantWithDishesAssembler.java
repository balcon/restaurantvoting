package com.github.balcon.restaurantvoting.dto.assembler;

import com.github.balcon.restaurantvoting.dto.DishDto;
import com.github.balcon.restaurantvoting.dto.RestaurantWithDishesDto;
import com.github.balcon.restaurantvoting.model.Restaurant;
import com.github.balcon.restaurantvoting.repository.VoteRepository;
import com.github.balcon.restaurantvoting.service.VoteService;
import com.github.balcon.restaurantvoting.web.rest.user.RestaurantUserController;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class RestaurantWithDishesAssembler {
    private final DishAssembler dishAssembler;
    private final VoteService service;

    public RestaurantWithDishesDto toModel(Restaurant r) {
        List<DishDto> dishDtos = r.getDishes().stream()
                .map(dishAssembler::toModel)
                .toList();
        RestaurantWithDishesDto dto = new RestaurantWithDishesDto(r.getName(), r.getAddress(), dishDtos);
        dto.add(WebMvcLinkBuilder.linkTo(methodOn(RestaurantUserController.class).getById(r.id())).withSelfRel());
        return dto;
    }

    public RestaurantWithDishesDto toModelWithVotes(Restaurant res) {
        RestaurantWithDishesDto dto = toModel(res);
        dto.setVotes(service.countVotesOfRest(res));
        return dto;
    }

    public RestaurantWithDishesDto toModelWithCollection(Restaurant r) {
        RestaurantWithDishesDto dto = toModelWithVotes(r);
        dto.add(linkTo(methodOn(RestaurantUserController.class).getAll())
                .withRel(IanaLinkRelations.COLLECTION));
        return dto;
    }

    public CollectionModel<RestaurantWithDishesDto> toCollectionModel(List<Restaurant> restaurants, LocalDate voteDate) {
        Map<Restaurant, Integer> votes = service.countVotesOfAllRests(voteDate).stream()
                .collect(Collectors.toMap(VoteRepository.VotesCount::getRestaurant, VoteRepository.VotesCount::getCount));
        CollectionModel<RestaurantWithDishesDto> dtos = CollectionModel.of(restaurants.stream()
                .map(r -> {
                    RestaurantWithDishesDto dto = toModel(r);
                    dto.setVotes(votes.getOrDefault(r, 0));
                    return dto;
                }).toList());
        dtos.add(linkTo(methodOn(RestaurantUserController.class).getAll()).withSelfRel());
        return dtos;
    }
}