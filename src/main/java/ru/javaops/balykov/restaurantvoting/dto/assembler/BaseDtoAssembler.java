package ru.javaops.balykov.restaurantvoting.dto.assembler;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import ru.javaops.balykov.restaurantvoting.model.BaseEntity;
import ru.javaops.balykov.restaurantvoting.web.rest.admin.UserController;

import java.util.ArrayList;
import java.util.Objects;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public abstract class BaseDtoAssembler<E extends BaseEntity, D extends RepresentationModel<?>>
        implements RepresentationModelAssembler<E, D> {

//    private final Class<BaseController<E>> controller;
//
//    protected BaseDtoAssembler(Class<BaseController<E>> controller) {
//        this.controller = controller;
//    }

    protected abstract D of(E entity);

    @Override
    public D toModel(E entity) {
        D model = toModelWithSelf(entity);
        model.add(linkTo(methodOn(UserController.class).getAll(Pageable.unpaged())).withRel("collection"));
        return model;
    }

    @Override
    public CollectionModel<D> toCollectionModel(Iterable<? extends E> entities) {
        ArrayList<D> dtos = new ArrayList<>();
        entities.forEach(e -> dtos.add(toModelWithSelf(e)));
        CollectionModel<D> models = CollectionModel.of(dtos);
        models.add(linkTo(methodOn(UserController.class).getAll(Pageable.unpaged())).withSelfRel());
        return models;
    }

    private D toModelWithSelf(E entity) {
        D model = of(entity);
        model.add(linkTo(methodOn(UserController.class).getById(Objects.requireNonNull(entity.getId()))).withSelfRel());
        return model;
    }
}