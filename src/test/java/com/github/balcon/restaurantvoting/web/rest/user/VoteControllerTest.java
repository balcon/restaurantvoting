package com.github.balcon.restaurantvoting.web.rest.user;

import com.github.balcon.restaurantvoting.repository.VoteRepository;
import com.github.balcon.restaurantvoting.util.DateTimeUtil;
import com.github.balcon.restaurantvoting.web.rest.BaseMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;

import static com.github.balcon.restaurantvoting.util.TestData.*;
import static com.github.balcon.restaurantvoting.web.rest.user.VoteController.BASE_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VoteControllerTest extends BaseMvcTest {
    @Autowired
    private VoteRepository repository;

    @Test
    @WithUserDetails(USER_EMAIL)
    void getNoVote() throws Exception {
        get(BASE_URL).andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void getTodaysVote() throws Exception {
        get(BASE_URL).andExpect(status().isOk())
                .andExpect(jsonPath("$.restaurant").exists());
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    void getYesterdaysVote() throws Exception {
        get(BASE_URL + "?vote_date=" + DateTimeUtil.currentDate().minusDays(1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.restaurant").exists());
    }

    @Test
    @WithUserDetails(USER_EMAIL)
    void vote() throws Exception {
        patch(BASE_URL + "?restaurant_id=" + REST_1_ID)
                .andExpect(status().isNoContent());

        assertThat(repository.findByUserAndVoteDate(USER, DateTimeUtil.currentDate())).isPresent();
    }
}