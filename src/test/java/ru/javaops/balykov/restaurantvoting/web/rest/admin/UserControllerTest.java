package ru.javaops.balykov.restaurantvoting.web.rest.admin;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.balykov.restaurantvoting.model.Role;
import ru.javaops.balykov.restaurantvoting.model.User;
import ru.javaops.balykov.restaurantvoting.repository.UserRepository;
import ru.javaops.balykov.restaurantvoting.web.rest.BaseControllerTest;

import java.util.List;
import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.balykov.restaurantvoting.util.TestData.*;
import static ru.javaops.balykov.restaurantvoting.web.rest.admin.UserController.BASE_URL;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest extends BaseControllerTest {

    UserRepository repository;

    public UserControllerTest(@Autowired UserRepository repository) {
        super(repository, BASE_URL);
        this.repository = repository;
    }

    @Test
    void create() throws Exception {
        User user = new User(NEW_USER);
        user.setRoles(Role.DEFAULT_ROLES);
        mockMvc.perform(post(BASE_URL)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(asJson(user))); // todo id?
    }

    @Test
    void createNotNew() throws Exception {
        super.createNotNew(USER);
    }

    @Test
    void getById() throws Exception {
        super.getById(USER_ID, USER);
    }

    @Test
    void getAll() throws Exception {
        super.getAll(List.of(USER, ADMIN));
    }

    @Test
    void getNotExists() throws Exception {
        super.getNotExists(-1);
    }

    @Test
    void deleteById() throws Exception {
        super.deleteById(USER_ID);
    }

    @Test
    void update() throws Exception {
        User user = new User(USER);
        int id = Objects.requireNonNull(user.getId());
        user.setName("New name");
        super.update(id, user);

        Assertions.assertThat(repository.findById(id).orElseThrow().getName())
                .isEqualTo("New name");
    }

    @Test
    void updateNotExists() throws Exception {
        User user = new User(USER);
        user.setId(-1);
        super.updateNotExists(user);
    }

    @Test
    void updateDifferentId() throws Exception {
        super.updateDifferentId(USER_ID, ADMIN);
    }
}