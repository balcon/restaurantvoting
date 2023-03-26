package ru.javaops.balykov.restaurantvoting.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;
import ru.javaops.balykov.restaurantvoting.model.User;
import ru.javaops.balykov.restaurantvoting.model.Vote;
import ru.javaops.balykov.restaurantvoting.repository.VoteRepository;

import java.util.Optional;

import static ru.javaops.balykov.restaurantvoting.util.DateTimeUtil.*;

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
                throw new IllegalStateException();
            }
        } else {
            repository.save(new Vote(restaurant, user));
        }
    }
}
