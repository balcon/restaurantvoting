package ru.javaops.balykov.restaurantvoting.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.javaops.balykov.restaurantvoting.util.validation.NoHtml;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString(callSuper = true)
public abstract class NamedEntity extends BaseEntity {
    @Column(nullable = false)
    @NotBlank
    @Size(min = 2, max = 64)
    @NoHtml
    protected String name;

    protected NamedEntity(Integer id, String name) {
        super(id);
        this.name = name;
    }
}