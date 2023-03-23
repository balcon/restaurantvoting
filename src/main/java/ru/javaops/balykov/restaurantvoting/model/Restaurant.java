package ru.javaops.balykov.restaurantvoting.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString(callSuper = true)
public class Restaurant extends AbstractEntity {
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<Dish> dishes;

    public Restaurant(Restaurant r) {
        this(r.id, r.name, r.description, r.address, new ArrayList<>());
    }

    public Restaurant(String name, String description, String address) {
        this(null, name, description, address, new ArrayList<>());
    }

    public Restaurant(Integer id, String name, String description, String address, List<Dish> dishes) {
        super(id, name);
        this.description = description;
        this.address = address;
        this.dishes = new ArrayList<>(dishes);
    }

    //todo DB indexes?!
    //todo unique
    //todo validation
}
