package ru.javaops.balykov.restaurantvoting.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString(callSuper = true)
public class Dish extends NamedEntity {

    private String description;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false, columnDefinition = "date default CURRENT_DATE()")
    private LocalDate offerDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @ToString.Exclude
    private Restaurant restaurant;

    public Dish(Dish d) {
        this(d.id, d.name, d.description, d.price, d.offerDate);
        restaurant = d.restaurant;
    }

    public Dish(String name, int price) {
        this(null, name, null, price);
    }

    public Dish(Integer id, String name, int price) {
        this(id, name, null, price);
    }

    public Dish(Integer id, String name, String description, int price) {
        this(id, name, description, price, LocalDate.now());
    }

    public Dish(Integer id, String name, String description, int price, LocalDate offerDate) {
        super(id, name);
        this.description = description;
        this.price = price;
        this.offerDate = offerDate;
    }
}
