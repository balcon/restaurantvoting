package ru.javaops.balykov.restaurantvoting.web.rest.admin;

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
import static ru.javaops.balykov.restaurantvoting.util.TestData.USER;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
//@RequiredArgsConstructor
abstract class AbstractControllerTest {
    private static final int BAD_ID = -1;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    private final JpaRepository<? extends BaseEntity, Integer> repository;

    private final String baseUrl;

    public AbstractControllerTest(JpaRepository<? extends BaseEntity, Integer> repository, String baseUrl) {
        this.repository = repository;
        this.baseUrl = baseUrl;
    }

    void create(BaseEntity entity) throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(entity)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(asJson(entity)));
    }

    void createNotNew(BaseEntity entity) throws Exception {
        mockMvc.perform(post(baseUrl)
                        .contentType(APPLICATION_JSON)
                        .content(asJson(entity)))
                .andDo(print())
                .andExpect(status().is(400));
    }

    void getById(int id, BaseEntity entity) throws Exception {
        mockMvc.perform(get(baseUrl + "/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(asJson(entity)));
    }

    void getAll(List<BaseEntity> entities) throws Exception {
        mockMvc.perform(get(baseUrl))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(entities)));
    }

    void getNotExists(int id) throws Exception {
        mockMvc.perform(get(baseUrl + "/" + id))
                .andDo(print())
                .andExpect(status().is(404));
    }

    void deleteById(int id) throws Exception {
        mockMvc.perform(delete(baseUrl + "/" + id))
                .andDo(print())
                .andExpect(status().isNoContent());

        Assertions.assertThat(repository.existsById(id)).isFalse();
    }

    void update(int id, BaseEntity baseEntity) throws Exception {
        mockMvc.perform(put(baseUrl + "/" + id)
                        .contentType(APPLICATION_JSON)
                        .content(asJson(baseEntity)))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    void updateNotExists(BaseEntity baseEntity) throws Exception {
        mockMvc.perform(put(baseUrl + "/" + BAD_ID)
                        .contentType(APPLICATION_JSON)
                        .content(asJson(baseEntity)))
                .andDo(print())
                .andExpect(status().is(404));
    }

    void updateDifferentId(int id, BaseEntity baseEntity) throws Exception {
        mockMvc.perform(put(baseUrl + "/" + id)
                        .contentType(APPLICATION_JSON)
                        .content(asJson(baseEntity)))
                .andDo(print())
                .andExpect(status().is(400));
    }

    protected String asJson(BaseEntity entity) throws JsonProcessingException {
        return mapper.writeValueAsString(entity);
    }
}