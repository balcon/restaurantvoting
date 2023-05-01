package ru.javaops.balykov.restaurantvoting.validation;

import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.javaops.balykov.restaurantvoting.exception.IllegalRequestException;
import ru.javaops.balykov.restaurantvoting.exception.NotFoundException;
import ru.javaops.balykov.restaurantvoting.model.BaseEntity;

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