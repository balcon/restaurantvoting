package com.github.balcon.restaurantvoting.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.Persistable;

import java.util.Objects;

@MappedSuperclass
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
public abstract class BaseEntity implements Persistable<Integer> {
    public static final int START_SEQ = 1000;
    @Id
    @SequenceGenerator(name = "main_seq", sequenceName = "main_seq", allocationSize = 1, initialValue = START_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "main_seq")
    @Column(updatable = false)
    protected Integer id;

    @Transient
    public Integer id() {
        return Objects.requireNonNull(id);
    }

    @Transient
    @Override
    @Schema(hidden = true)
    public boolean isNew() {
        return id == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}