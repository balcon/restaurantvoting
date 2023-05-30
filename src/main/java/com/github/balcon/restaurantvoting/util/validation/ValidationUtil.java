package com.github.balcon.restaurantvoting.util.validation;

import com.github.balcon.restaurantvoting.exception.IllegalRequestException;
import com.github.balcon.restaurantvoting.exception.NotFoundException;
import com.github.balcon.restaurantvoting.model.BaseEntity;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.repository.JpaRepository;

@UtilityClass
public class ValidationUtil {
    public void checkNew(BaseEntity entity) {
        if (!entity.isNew()) {
            throw new IllegalRequestException(entity.getClass().getSimpleName() + " must be new");
        }
    }

    public void assureIdConsistent(BaseEntity entity, int id) {
        if (entity.isNew()) {
            entity.setId(id);
        } else if (!entity.id().equals(id)) {
            throw new IllegalRequestException(entity.getClass().getSimpleName() + " must have id = " + id);
        }
    }

    public void checkIfExists(JpaRepository<?, Integer> repository, int id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException(id);
        }
    }
}