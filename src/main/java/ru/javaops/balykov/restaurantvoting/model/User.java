package ru.javaops.balykov.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString(callSuper = true)
public class User extends NamedEntity {
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ToString.Exclude
    private String password;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false)
    @BatchSize(size = 20) //todo EntityGraph?
    private Set<Role> roles;

    public User(User u) {
        this(u.id, u.name, u.email, u.password, u.roles); // todo copy collection
    }

    public User(String name, String email, String password) {
        this(null, name, email, password, null);
    }

    public User(String name, String email, String password, Set<Role> roles) {
        this(null, name, email, password, roles);
    }

    public User(Integer id, String name, String email, String password, Set<Role> roles) {
        super(id, name);
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
}
