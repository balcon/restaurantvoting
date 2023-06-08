package com.github.balcon.restaurantvoting.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.balcon.restaurantvoting.BaseTest;
import com.github.balcon.restaurantvoting.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.balcon.restaurantvoting.exception.IllegalRequestException.ILLEGAL_REQUEST;
import static com.github.balcon.restaurantvoting.exception.NotFoundException.NOT_FOUND;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public abstract class BaseMvcTest extends BaseTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper mapper;

    protected final ResultActions get(String uri) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(uri))
                .andDo(print());
    }

    protected final ResultActions patch(String uri) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.patch(uri))
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

    protected void expectNotFound(ResultActions resultActions) throws Exception {
        resultActions
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value(NOT_FOUND));
    }

    protected void expectIllegalRequest(ResultActions resultActions) throws Exception {
        resultActions
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.type").value(ILLEGAL_REQUEST));
    }

    private String asJson(Object object) throws JsonProcessingException {
        if (object instanceof User) {
            return asJsonWithPassword((User) object);
        }
        return mapper.writeValueAsString(object);
    }

    // Writing password to json manually because password is write-only in jackson config
    private String asJsonWithPassword(User user) {
        ObjectNode json = mapper.valueToTree(user);
        json.put("password", user.getPassword());
        return json.toString();
    }
}