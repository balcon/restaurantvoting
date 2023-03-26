package ru.javaops.balykov.restaurantvoting.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.javaops.balykov.restaurantvoting.util.DateTimeUtil;

import java.time.LocalDate;

@Entity
@Table(name = "votes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString(callSuper = true)
public class Vote extends BaseEntity { // todo change ID to restaurant + voteDate
    @ManyToOne(fetch = FetchType.EAGER) // todo think about fetch
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.EAGER) // todo think about fetch
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(nullable = false, columnDefinition = "date default CURRENT_DATE()")
    private LocalDate voteDate;

    public Vote(Restaurant restaurant, User user) {
        this(null, restaurant, user);
    }

    public Vote(Integer id, Restaurant restaurant, User user) {
        super(id);
        this.restaurant = restaurant;
        this.user = user;
        this.voteDate = DateTimeUtil.currentDate();
    }
}
