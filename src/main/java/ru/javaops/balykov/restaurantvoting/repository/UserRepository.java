package ru.javaops.balykov.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javaops.balykov.restaurantvoting.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
