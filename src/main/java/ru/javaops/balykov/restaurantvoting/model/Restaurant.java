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
public class Restaurant extends NamedEntity {
    @Column(nullable = false)
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<Dish> dishes; //todo BatchSize?

    public Restaurant(Restaurant r) {
        this(r.id, r.name, r.address, new ArrayList<>());
    }

    public Restaurant(String name, String address) {
        this(null, name, address, new ArrayList<>());
    }

    public Restaurant(Integer id, String name, String address, List<Dish> dishes) {
        super(id, name);
        this.address = address;
        this.dishes = dishes;
    }

    //todo DB indexes?!
    //todo unique
    //todo validation
}
