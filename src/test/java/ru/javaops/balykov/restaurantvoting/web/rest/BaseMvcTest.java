package ru.javaops.balykov.restaurantvoting.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public abstract class BaseMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper mapper;

    protected final ResultActions get(String uri) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(uri))
                .andDo(print());
    }

    protected final ResultActions put(String uri) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put(uri))
                .andDo(print());
    }

    protected final ResultActions put(String uri, Object object) throws Exception {
        return put(uri, asJson(object));
    }

    protected final ResultActions put(String uri, String json) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.put(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print());
    }

    protected final ResultActions post(String uri, Object object) throws Exception {
        return post(uri, asJson(object));
    }

    protected final ResultActions post(String uri, String json) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(uri)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print());
    }

    protected final ResultActions delete(String uri) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.delete(uri))
                .andDo(print());
    }

    protected final ResultMatcher match(Object object) throws JsonProcessingException {
        return content().json(asJson(object));
    }

    private String asJson(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

}
