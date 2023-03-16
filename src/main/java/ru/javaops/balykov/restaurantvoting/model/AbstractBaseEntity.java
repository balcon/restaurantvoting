package ru.javaops.balykov.restaurantvoting.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractBaseEntity {
    public static final int START_SEQ = 1000;
    @Id
    @SequenceGenerator(name = "main_seq", sequenceName = "main_seq",
            allocationSize = 1, initialValue = START_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "main_seq")
    private Integer id;
}
