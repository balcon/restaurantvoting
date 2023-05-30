package com.github.balcon.restaurantvoting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.balcon.restaurantvoting.exception.IllegalRequestException;
import com.github.balcon.restaurantvoting.model.Restaurant;
import com.github.balcon.restaurantvoting.model.User;
import com.github.balcon.restaurantvoting.model.Vote;
import com.github.balcon.restaurantvoting.repository.VoteRepository;

import java.util.Optional;

import static com.github.balcon.restaurantvoting.util.DateTimeUtil.*;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository repository;

    @Transactional
    public void vote(Restaurant restaurant, User user) {
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