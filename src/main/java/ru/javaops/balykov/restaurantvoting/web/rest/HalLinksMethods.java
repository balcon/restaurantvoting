package ru.javaops.balykov.restaurantvoting.web.rest;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;

public interface HalLinksMethods {
    RepresentationModel<?> getById(int id);

    CollectionModel<? extends RepresentationModel<?>> getAll(Pageable pageable);
}