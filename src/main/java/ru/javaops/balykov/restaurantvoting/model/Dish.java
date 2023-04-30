package ru.javaops.balykov.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import ru.javaops.balykov.restaurantvoting.util.DateTimeUtil;

import java.time.LocalDate;

@Entity
@Table(name = "dishes",
        indexes = @Index(name = "dishes_rest_date_idx", columnList = "restaurant_id, offerDate"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString(callSuper = true)
public class Dish extends NamedEntity {
    @Column(nullable = false)
    @NotNull
    @Range(min = 0, max = 200000)
    private Integer price;

    @Column(nullable = false, columnDefinition = "date default CURRENT_DATE()")
    private LocalDate offerDate = DateTimeUtil.currentDate();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private Restaurant restaurant;

    public Dish(Dish dish) {
        this(dish.id, dish.name, dish.price, dish.offerDate);
        this.restaurant = dish.restaurant;
    }

    public Dish(Integer id, String name, int price) {
        super(id, name);
        this.price = price;
    }

    public Dish(Integer id, String name, int price, LocalDate offerDate) {
        this(id, name, price);
        this.offerDate = offerDate;
    }
}