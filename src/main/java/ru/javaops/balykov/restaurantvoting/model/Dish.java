package ru.javaops.balykov.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Range;
import ru.javaops.balykov.restaurantvoting.util.DateTimeUtil;

import java.time.LocalDate;

@Entity
@Table(name = "dishes")
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
    private LocalDate offerDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private Restaurant restaurant;

    public Dish(Dish d) {
        this(d.id, d.name, d.price, d.offerDate);
        restaurant = d.restaurant;
    }

    public Dish(String name, int price) {
        this(null, name, price);
    }

    public Dish(Integer id, String name, int price) {
        this(id, name, price, DateTimeUtil.currentDate());
    }

    public Dish(Integer id, String name, int price, LocalDate offerDate) {
        super(id, name);
        this.price = price;
        this.offerDate = offerDate;
    }
}
