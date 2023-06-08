package com.github.balcon.restaurantvoting.service;

import com.github.balcon.restaurantvoting.exception.IllegalRequestException;
import com.github.balcon.restaurantvoting.model.Restaurant;
import com.github.balcon.restaurantvoting.model.User;
import com.github.balcon.restaurantvoting.model.Vote;
import com.github.balcon.restaurantvoting.repository.VoteRepository;
import com.github.balcon.restaurantvoting.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.github.balcon.restaurantvoting.util.DateTimeUtil.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteService {
    public static final String VOTES_CACHE = "votesCache";
    public static final LocalTime REVOTE_DEADLINE = LocalTime.of(11, 0);

    private final VoteRepository repository;
    private final RestaurantService restaurantService;

    public Optional<Vote> get(User user, LocalDate voteDate) {
        log.info("User [{}] get their vote by date [{}]", user, voteDate);
        return repository.findWithRestaurant(user, voteDate);
    }

    @CacheEvict(cacheNames = VOTES_CACHE, allEntries = true)
    @Transactional
    public void doVote(int restaurantId, User user) {
        log.info("User [{}] vote by restaurant with id [{}]", user, restaurantId);
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

    public int countVotesOfRest(Restaurant restaurant) {
        return repository.countByRestaurantAndVoteDate(restaurant, DateTimeUtil.currentDate());
    }

    @Cacheable(cacheNames = VOTES_CACHE, key = "#voteDate")
    public List<VoteRepository.VotesCount> countVotesOfAllRests(LocalDate voteDate) {
        return repository.countOfAllByVoteDate(voteDate);
    }
}