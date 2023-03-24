package ru.javaops.balykov.restaurantvoting.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class User extends NamedEntity {
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false)
    @BatchSize(size = 20) //todo EntityGraph?
    private Set<Role> roles;

    public User(User u) {
        this(u.id, u.name, u.email, u.password, Set.copyOf(u.roles));
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
