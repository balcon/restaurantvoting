package com.github.balcon.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.balcon.restaurantvoting.util.validation.NoHtml;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name", "address"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString(callSuper = true)
public class Restaurant extends NamedEntity {
    @Column(nullable = false)
    @NotBlank
    @Size(min = 8, max = 128)
    @NoHtml
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    @JsonIgnore
    private List<Dish> dishes;

    public Restaurant(Restaurant r) {
        this(r.id, r.name, r.address, r.dishes);
    }

    public Restaurant(Integer id, String name, String address) {
        super(id, name);
        this.address = address;
    }

    public Restaurant(Integer id, String name, String address, List<Dish> dishes) {
        this(id, name, address);
        this.dishes = dishes;
    }
}