package com.github.balcon.restaurantvoting.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import com.github.balcon.restaurantvoting.util.validation.NoHtml;

@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString(callSuper = true)
public abstract class NamedEntity extends BaseEntity {
    @Column(nullable = false)
    @NotBlank
    @Size(min = 2, max = 128)
    @NoHtml
    protected String name;

    protected NamedEntity(Integer id, String name) {
        super(id);
        this.name = name;
    }
}