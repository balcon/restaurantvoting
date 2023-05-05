package ru.javaops.balykov.restaurantvoting.dto.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import ru.javaops.balykov.restaurantvoting.model.BaseEntity;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public abstract class BaseAssembler<E extends BaseEntity, D extends RepresentationModel<?>>
        implements RepresentationModelAssembler<E, D> {

    private final Class<? extends MethodsForAssembler> controller;
    @Autowired
    // Suppress IntelliJ IDEA Error: Could not autowire. No beans of 'PagedResourcesAssembler<E>' type found.
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private PagedResourcesAssembler<E> pagedAssembler;

    protected BaseAssembler(Class<? extends MethodsForAssembler> controller) {
        this.controller = controller;
    }

    protected abstract D of(E entity);

    @Override
    public D toModel(E entity) {
        D model = of(entity);
        model.add(linkTo(methodOn(controller).getById(entity.id())).withSelfRel());
        return model;
    }

    public D toModelWithCollection(E entity) {
        D model = toModel(entity);
        model.add(linkTo(methodOn(controller).getAll(Pageable.unpaged())).withRel(IanaLinkRelations.COLLECTION));
        return model;
    }

    public CollectionModel<D> toCollectionModel(Page<E> entities) {
        return pagedAssembler.toModel(entities, this);
    }
}