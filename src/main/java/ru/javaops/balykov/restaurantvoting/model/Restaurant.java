package ru.javaops.balykov.restaurantvoting.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Restaurant extends AbstractPersistable<Integer> {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Dish> dishes;

    public Restaurant(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
