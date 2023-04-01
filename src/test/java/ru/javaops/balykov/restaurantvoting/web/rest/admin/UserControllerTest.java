package ru.javaops.balykov.restaurantvoting.web.rest.admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.balykov.restaurantvoting.model.User;
import ru.javaops.balykov.restaurantvoting.repository.UserRepository;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javaops.balykov.restaurantvoting.util.TestData.*;
import static ru.javaops.balykov.restaurantvoting.web.rest.admin.UserController.BASE_URL;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private UserRepository repository;

    @Test
    void getAll() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ADMIN_ID))
                .andExpect(jsonPath("$[1].id").value(USER_ID))
                .andExpect(content().json(mapper.writeValueAsString(List.of(USER, ADMIN))));
    }

    @Test
    void getById() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + USER_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(USER)));
    }

    @Test
    void getNotExists() throws Exception {
        mockMvc.perform(get(BASE_URL + "/0"))
                .andDo(print())
                .andExpect(status().is(404));
    }

    @Test
    void deleteById() throws Exception {
        mockMvc.perform(delete(BASE_URL + "/" + USER_ID))
                .andDo(print())
                .andExpect(status().isNoContent());

        Assertions.assertThat(repository.existsById(USER_ID)).isFalse();
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(APPLICATION_JSON)
                        .content(asJson(NEW_USER)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(asJson(NEW_USER)));
    }

    @Test
    void createNotNew() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(APPLICATION_JSON)
                        .content(asJson(USER)))
                .andDo(print())
                .andExpect(status().is(400));
    }

    @Test
    void update() throws Exception {
        User user = new User(USER);
        Integer id = user.getId();
        user.setName("Updated name");

        mockMvc.perform(put(BASE_URL + "/" + id)
                        .contentType(APPLICATION_JSON)
                        .content(asJson(user)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void updateNotExists() throws Exception {
        mockMvc.perform(put(BASE_URL + "/" + NON_EXISTENT_ID)
                        .contentType(APPLICATION_JSON)
                        .content(asJson(USER)))
                .andDo(print())
                .andExpect(status().is(404));
    }

    @Test
    void updateDifferentId() throws Exception {
        mockMvc.perform(put(BASE_URL + "/" + ADMIN_ID)
                        .contentType(APPLICATION_JSON)
                        .content(asJson(USER)))
                .andDo(print())
                .andExpect(status().is(400));
    }

    private String asJson(User user) throws JsonProcessingException {
        return mapper.writeValueAsString(user);
    }
}