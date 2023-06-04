package com.github.balcon.restaurantvoting.repository;

import com.github.balcon.restaurantvoting.model.Restaurant;
import com.github.balcon.restaurantvoting.model.User;
import com.github.balcon.restaurantvoting.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends JpaRepository<Vote, Integer> {
    Optional<Vote> findByUserAndVoteDate(User user, LocalDate voteDate);


    int countByRestaurantAndVoteDate(Restaurant restaurant, LocalDate voteDate);

    @Query("from Vote v join fetch v.restaurant where v.user = :user and v.voteDate = :voteDate")
    Optional<Vote> findWithRestaurant(User user, LocalDate voteDate);

    @Query("select v.restaurant as restaurant, count(v.id) as count from Vote v " +
            "where v.voteDate = :voteDate and v.restaurant in :restaurants group by v.restaurant")
    List<VotesCount> countRestaurantsVotes(List<Restaurant> restaurants, LocalDate voteDate);

    interface VotesCount {
        Restaurant getRestaurant();
        int getCount();
    }
}