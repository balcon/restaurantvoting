package ru.javaops.balykov.restaurantvoting.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString(callSuper = true)
public class Dish extends AbstractEntity {
    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Integer price;

    @Column(columnDefinition = "date default CURRENT_DATE() not null")
    private LocalDate offerDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private Restaurant restaurant;

    public Dish(Integer id, String name, int price) {
        this(id, name, null, price);
    }

    public Dish(Integer id, String name, String description, int price) {
        super(id);
        this.name = name;
        this.description = description;
        this.price = price;
        this.offerDate = LocalDate.now();
    }
}
