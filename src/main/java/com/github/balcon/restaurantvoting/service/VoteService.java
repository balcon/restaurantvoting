package com.github.balcon.restaurantvoting.service;

import com.github.balcon.restaurantvoting.exception.IllegalRequestException;
import com.github.balcon.restaurantvoting.exception.NotFoundException;
import com.github.balcon.restaurantvoting.model.Restaurant;
import com.github.balcon.restaurantvoting.model.User;
import com.github.balcon.restaurantvoting.model.Vote;
import com.github.balcon.restaurantvoting.repository.RestaurantRepository;
import com.github.balcon.restaurantvoting.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static com.github.balcon.restaurantvoting.util.DateTimeUtil.*;

@Service
@RequiredArgsConstructor
public class VoteService {
    public static final LocalTime REVOTE_DEADLINE = LocalTime.of(11, 0);

    private final VoteRepository repository;

    private final RestaurantRepository restaurantRepository;

    public Optional<Vote> get(User user, LocalDate voteDate) {
        return repository.findWithRestaurant(user, voteDate);
    }

    @Transactional
    public void vote(int restaurant_id, User user) {
        Restaurant restaurant = restaurantRepository.findById(restaurant_id).
                orElseThrow(() -> new NotFoundException(restaurant_id));
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
}