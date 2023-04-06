package ru.javaops.balykov.restaurantvoting.web.rest.admin;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.javaops.balykov.restaurantvoting.model.BaseEntity;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public abstract class BaseController<T extends BaseEntity> {
    private final JpaRepository<T, Integer> repository;

    private final Logger log;

    public ResponseEntity<T> create(T entity) {
        log.info("Create [{}]", entity);
        if (!entity.isNew()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(repository.save(entity), HttpStatus.CREATED);
    }

    public ResponseEntity<T> getById(int id) {
        log.info("Get by id [{}]", id);
        return ResponseEntity.of(repository.findById(id));
    }

    public List<T> getAll(Sort sort) {
        log.info("Get all");
        return repository.findAll(sort);
    }

    public ResponseEntity<?> update(int id, T entity) {
        log.info("Update [{}] with id [{}]", entity, id);
        if (entity.isNew()) {
            entity.setId(id);
        } else if (!Objects.equals(entity.getId(), id)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (!repository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        repository.save(entity);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public void delete(int id) {
        log.info("Delete with id [{}]", id);
        repository.deleteById(id);
    }
}