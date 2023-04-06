package ru.javaops.balykov.restaurantvoting.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.balykov.restaurantvoting.model.BaseEntity;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
//@RequiredArgsConstructor
public abstract class BaseControllerTest {
    private static final int BAD_ID = -1;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper mapper;

    private final JpaRepository<? extends BaseEntity, Integer> repository;

    private final String baseUrl;

    public BaseControllerTest(JpaRepository<? extends BaseEntity, Integer> repository, String baseUrl) {
        this.repository = repository;
        this.baseUrl = baseUrl;
    }

    protected void create(BaseEntity entity) throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(entity)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(asJson(entity)));
    }

    protected void createNotNew(BaseEntity entity) throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(APPLICATION_JSON)
                        .content(asJson(entity)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    protected void getById(int id, BaseEntity entity) throws Exception {
        mockMvc.perform(get(baseUrl + "/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(entity)));
    }

    protected void getAll(List<BaseEntity> entities) throws Exception {
        mockMvc.perform(get(baseUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(entities)));
    }

    protected void getNotExists(int id) throws Exception {
        mockMvc.perform(get(baseUrl + "/" + id))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    protected void deleteById(int id) throws Exception {
        mockMvc.perform(delete(baseUrl + "/" + id))
                .andDo(print())
                .andExpect(status().isNoContent());

        Assertions.assertThat(repository.existsById(id)).isFalse();
    }

    protected void update(int id, BaseEntity baseEntity) throws Exception {
        mockMvc.perform(put(baseUrl + "/" + id)
                        .contentType(APPLICATION_JSON)
                        .content(asJson(baseEntity)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    protected void updateNotExists(BaseEntity baseEntity) throws Exception {
        mockMvc.perform(put(baseUrl + "/" + BAD_ID)
                        .contentType(APPLICATION_JSON)
                        .content(asJson(baseEntity)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    protected void updateDifferentId(int id, BaseEntity baseEntity) throws Exception {
        mockMvc.perform(put(baseUrl + "/" + id)
                        .contentType(APPLICATION_JSON)
                        .content(asJson(baseEntity)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    protected String asJson(BaseEntity entity) throws JsonProcessingException {
        return mapper.writeValueAsString(entity);
    }
}