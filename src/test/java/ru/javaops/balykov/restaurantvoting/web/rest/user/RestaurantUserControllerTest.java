package ru.javaops.balykov.restaurantvoting.web.rest.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import ru.javaops.balykov.restaurantvoting.repository.VoteRepository;
import ru.javaops.balykov.restaurantvoting.util.DateTimeUtil;
import ru.javaops.balykov.restaurantvoting.web.rest.BaseMvcTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javaops.balykov.restaurantvoting.util.TestData.*;
import static ru.javaops.balykov.restaurantvoting.web.rest.user.RestaurantUserController.BASE_URL;

class RestaurantUserControllerTest extends BaseMvcTest {
    @Autowired
    private VoteRepository voteRepository;

    @Test
    void getById() throws Exception {
        get(BASE_URL + "/" + REST_1_ID)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(match(REST_1_TO));
    }

    @Test
    void getTodayWithDishesAndVotes() throws Exception {
        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(match(List.of(REST_2_TO, REST_1_TO)));
    }

    @Test
    void voting() throws Exception {
        LocalDate today = DateTimeUtil.currentDate();
        int count = voteRepository.findByRestaurantAndVoteDate(REST_1, today).size();

        put(BASE_URL + "/" + REST_1_ID + "/voted")
                .andExpect(status().isNoContent());
        assertThat(voteRepository.findByRestaurantAndVoteDate(REST_1, today)).hasSize(count+1);
    }
}