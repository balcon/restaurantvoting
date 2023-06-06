package com.github.balcon.restaurantvoting.service;

import com.github.balcon.restaurantvoting.exception.IllegalRequestException;
import com.github.balcon.restaurantvoting.model.Restaurant;
import com.github.balcon.restaurantvoting.model.User;
import com.github.balcon.restaurantvoting.model.Vote;
import com.github.balcon.restaurantvoting.repository.VoteRepository;
import com.github.balcon.restaurantvoting.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.github.balcon.restaurantvoting.util.DateTimeUtil.currentDate;
import static com.github.balcon.restaurantvoting.util.DateTimeUtil.currentTime;

@Service
@RequiredArgsConstructor
public class VoteService {
    public static final LocalTime REVOTE_DEADLINE = LocalTime.of(11, 0);

    private final VoteRepository repository;
    private final RestaurantService restaurantService;

    public Optional<Vote> get(User user, LocalDate voteDate) {
        return repository.findWithRestaurant(user, voteDate);
    }

    @Transactional
    public void doVote(int restaurantId, User user) {
        Restaurant restaurant = restaurantService.get(restaurantId);
        Optional<Vote> voteOptional = repository.findByUserAndVoteDate(user, currentDate());
        if (voteOptional.isPresent()) {
            if (currentTime().isBefore(REVOTE_DEADLINE)) {
                Vote vote = voteOptional.get();
                vote.setRestaurant(restaurant);
                repository.save(vote);
            } else {
                throw new IllegalRequestException("Time for revote is over");
            }
        } else {
            repository.save(new Vote(restaurant, user));
        }
    }

    public int count(Restaurant restaurant) {
        return repository.countByRestaurantAndVoteDate(restaurant, DateTimeUtil.currentDate());
    }

    public List<VoteRepository.VotesCount> countAll(List<Restaurant> restaurants) {
        return repository.countRestaurantsVotes(restaurants, DateTimeUtil.currentDate());
    }
}