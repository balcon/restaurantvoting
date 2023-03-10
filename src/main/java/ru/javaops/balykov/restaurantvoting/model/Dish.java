package ru.javaops.balykov.restaurantvoting.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Dish extends AbstractPersistable<Integer> {
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;



    public Dish(String name, Integer price) {
        this.name = name;
        this.price = price;
    }
}
