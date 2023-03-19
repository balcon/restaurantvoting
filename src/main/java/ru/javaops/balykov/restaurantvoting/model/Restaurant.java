package ru.javaops.balykov.restaurantvoting.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString(callSuper = true)
public class Restaurant extends AbstractEntity {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Dish> dishes;

    public Restaurant(Integer id, String name, String description, String address, List<Dish> dishes) {
        super(id);
        this.name = name;
        this.description = description;
        this.address = address;
        this.dishes = new ArrayList<>(dishes);
    }

    //todo DB indexes?!
    //todo validation
}
