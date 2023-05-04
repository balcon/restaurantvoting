package ru.javaops.balykov.restaurantvoting.dto.assembler;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.stereotype.Component;
import ru.javaops.balykov.restaurantvoting.dto.DishDto;
import ru.javaops.balykov.restaurantvoting.dto.RestaurantWithDishesDto;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;
import ru.javaops.balykov.restaurantvoting.repository.VoteRepository;
import ru.javaops.balykov.restaurantvoting.web.rest.user.RestaurantUserController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class RestaurantWithDishes {
    private final DishAssembler dishAssembler;
    private final VoteRepository voteRepository;

    public RestaurantWithDishesDto toModel(Restaurant r) {
        List<DishDto> dishDtos = r.getDishes().stream()
                .map(dishAssembler::of)
                .toList();
        RestaurantWithDishesDto dto = new RestaurantWithDishesDto(r.getName(), r.getAddress(), dishDtos);
        dto.add(linkTo(methodOn(RestaurantUserController.class).getById(r.id(), Sort.unsorted())).withSelfRel());
        return dto;
    }

    public RestaurantWithDishesDto toModel(Restaurant r, LocalDate voteDate) {
        RestaurantWithDishesDto dto = toModel(r);
        dto.setVotes(voteRepository.countByRestaurantAndVoteDate(r, voteDate));
        return dto;
    }

    public RestaurantWithDishesDto toModelWithCollection(Restaurant r, LocalDate voteDate) {
        RestaurantWithDishesDto dto = toModel(r, voteDate);
        dto.add(linkTo(methodOn(RestaurantUserController.class).getAll(Sort.unsorted()))
                .withRel(IanaLinkRelations.COLLECTION));
        return dto;
    }

    public CollectionModel<RestaurantWithDishesDto> toCollectionModel(List<Restaurant> restaurants, LocalDate voteDate) {
        Map<Restaurant, Integer> votes = voteRepository.countRestaurantsVotes(restaurants, voteDate).stream()
                .collect(Collectors.toMap(VoteRepository.VotesCount::getRestaurant, VoteRepository.VotesCount::getCount));
        CollectionModel<RestaurantWithDishesDto> dtos = CollectionModel.of(restaurants.stream()
                .map(r -> {
                    RestaurantWithDishesDto dto = toModel(r);
                    dto.setVotes(votes.getOrDefault(r, 0));
                    return dto;
                }).toList());
        dtos.add(linkTo(methodOn(RestaurantUserController.class).getAll(Sort.unsorted())).withSelfRel());
        return dtos;
    }
}