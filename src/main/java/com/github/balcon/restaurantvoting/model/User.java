package com.github.balcon.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.balcon.restaurantvoting.util.validation.NoHtml;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank
    @Email
    @NoHtml
    private String email;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ToString.Exclude
    @NotBlank
    @Size(min = 8)
    private String password;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false)
    @BatchSize(size = 20)
    private Set<Role> roles;

    public User(User u) {
        this(u.id, u.name, u.email, u.password, u.roles);
    }

    public User(String name, String email, String password) {
        this(null, name, email, password, Role.DEFAULT_ROLES);
    }

    public User(Integer id, String name, String email, String password, Set<Role> roles) {
        super(id, name);
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }
}