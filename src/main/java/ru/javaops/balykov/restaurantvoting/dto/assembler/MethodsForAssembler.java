package ru.javaops.balykov.restaurantvoting.dto.assembler;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;

public interface MethodsForAssembler {
    RepresentationModel<?> getById(int id);

    CollectionModel<? extends RepresentationModel<?>> getAll(Pageable pageable);
}