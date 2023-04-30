package ru.javaops.balykov.restaurantvoting.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;
import ru.javaops.balykov.restaurantvoting.model.Vote;
import ru.javaops.balykov.restaurantvoting.repository.VoteRepository;
import ru.javaops.balykov.restaurantvoting.to.RestaurantTo;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RestaurantToMapper {
    private final VoteRepository repository;

    public RestaurantTo of(Restaurant restaurant) {
        List<Vote> votes = repository.findByRestaurantAndVoteDate(restaurant, DateTimeUtil.currentDate());
        return getTo(restaurant, votes);
    }

    public List<RestaurantTo> of(List<Restaurant> restaurants) {
        Map<Restaurant, List<Vote>> votes =
                repository.findByVoteDate(DateTimeUtil.currentDate()).stream()
                        .collect(Collectors.groupingBy(Vote::getRestaurant));
        return restaurants.stream()
                .map(r -> getTo(r, votes.getOrDefault(r, Collections.emptyList())))
                .collect(Collectors.toList());
    }

    private RestaurantTo getTo(Restaurant restaurant, List<Vote> votes) {
        return new RestaurantTo(restaurant.getId(), restaurant.getName(),
                restaurant.getAddress(), restaurant.getDishes(), votes);
    }
}