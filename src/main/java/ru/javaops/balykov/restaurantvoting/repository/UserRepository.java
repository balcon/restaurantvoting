package ru.javaops.balykov.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.balykov.restaurantvoting.model.User;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {
}
