package com.github.balcon.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.balcon.restaurantvoting.util.DateTimeUtil;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "voteDate"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class Vote extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @Column(nullable = false, columnDefinition = "date default CURRENT_DATE()")
    private LocalDate voteDate = DateTimeUtil.currentDate();

    public Vote(Restaurant restaurant, User user) {
        this(null, restaurant, user);
    }

    public Vote(Integer id, Restaurant restaurant, User user) {
        super(id);
        this.restaurant = restaurant;
        this.user = user;
    }
}