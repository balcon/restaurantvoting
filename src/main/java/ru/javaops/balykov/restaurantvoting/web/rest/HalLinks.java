package ru.javaops.balykov.restaurantvoting.web.rest;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;

public interface HalLinks<T extends RepresentationModel<?>> {
    T getById(int id);

    CollectionModel<T> getAll(Pageable pageable);
}