package ru.javaops.balykov.restaurantvoting.web.rest.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.javaops.balykov.restaurantvoting.repository.VoteRepository;
import ru.javaops.balykov.restaurantvoting.util.DateTimeUtil;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.balykov.restaurantvoting.util.TestData.*;
import static ru.javaops.balykov.restaurantvoting.web.rest.user.RestaurantUserController.BASE_URL;

@SpringBootTest
@AutoConfigureMockMvc
class RestaurantUserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private VoteRepository voteRepository;

    @Test
    void getById() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + REST_1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(REST_1_TO)));
    }

    @Test
    void getTodayWithDishesAndVotes() throws Exception {
        mockMvc.perform(get(BASE_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(
                        mapper.writeValueAsString(List.of(REST_2_TO, REST_1_TO))));
    }

    @Test
    void legalVote() throws Exception {
        LocalDate today = DateTimeUtil.currentDate();

        assertThat(voteRepository.findByRestaurantAndVoteDate(REST_1, today)).hasSize(1);

        mockMvc.perform(put(BASE_URL + "/" + REST_1_ID + "/voted"))
                .andDo(print())
                .andExpect(status().isNoContent());

        assertThat(voteRepository.findByRestaurantAndVoteDate(REST_1, today)).hasSize(2);
    }
}