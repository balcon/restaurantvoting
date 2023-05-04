package ru.javaops.balykov.restaurantvoting.web.rest;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.javaops.balykov.restaurantvoting.exception.NotFoundException;
import ru.javaops.balykov.restaurantvoting.model.BaseEntity;
import ru.javaops.balykov.restaurantvoting.validation.ValidationUtil;

import java.util.Optional;

@RequiredArgsConstructor
public abstract class BaseController<T extends BaseEntity> {

    private final JpaRepository<T, Integer> repository;
    private final Logger log;

    protected T doCreate(T entity) {
        log.info("Create [{}]", entity);
        ValidationUtil.checkNew(entity);
        return repository.save(entity);
    }

    protected T doGetById(int id) {
        log.info("Get by id [{}]", id);
        Optional<T> optionalEntity = repository.findById(id);
        if (optionalEntity.isEmpty()) {
            throw new NotFoundException(id);
        }
        return optionalEntity.get();
    }

    protected Page<T> doGetAll(Pageable pageable) {
        log.info("Get all with pagination [{}]", pageable);
        return repository.findAll(pageable);
    }

    // @Transactional
    // Transaction breaks custom validators
    protected void doUpdate(int id, T entity) {
        log.info("Update [{}] with id [{}]", entity, id);
        ValidationUtil.assureIdConsistent(entity, id);
        ValidationUtil.checkIfExists(repository, id);
        repository.save(entity);
    }

    // @Transactional
    // Transaction breaks custom validators
    protected void doDelete(int id) {
        log.info("Delete with id [{}]", id);
        ValidationUtil.checkIfExists(repository, id);
        repository.deleteById(id);
    }
}