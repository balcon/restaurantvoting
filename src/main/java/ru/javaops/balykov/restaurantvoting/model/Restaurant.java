package ru.javaops.balykov.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "restaurants",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "address"}),
        indexes = @Index(name = "restaurants_name_idx", columnList = "name"))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString(callSuper = true)
public class Restaurant extends NamedEntity {
    @Column(nullable = false)
    @NotBlank
    @Size(min = 8, max = 64)
    private String address;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    @JsonIgnore
    private List<Dish> dishes;

    public Restaurant(Integer id, String name, String address) {
        super(id, name);
        this.address = address;
    }

    public Restaurant(Integer id, String name, String address, List<Dish> dishes) {
        this(id, name, address);
        this.dishes = dishes;
    }
}