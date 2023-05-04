package ru.javaops.balykov.restaurantvoting.web.rest.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.security.test.context.support.WithUserDetails;
import ru.javaops.balykov.restaurantvoting.dto.RestaurantWithDishesDto;
import ru.javaops.balykov.restaurantvoting.repository.VoteRepository;
import ru.javaops.balykov.restaurantvoting.util.DateTimeUtil;
import ru.javaops.balykov.restaurantvoting.web.rest.BaseMvcTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.hateoas.IanaLinkRelations.COLLECTION_VALUE;
import static org.springframework.hateoas.IanaLinkRelations.SELF_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javaops.balykov.restaurantvoting.util.TestData.*;
import static ru.javaops.balykov.restaurantvoting.web.rest.user.RestaurantUserController.BASE_URL;

@WithUserDetails(USER_EMAIL)
class RestaurantUserControllerTest extends BaseMvcTest {
    @Autowired
    private VoteRepository voteRepository;

    @Test
    void getById() throws Exception {
        get(BASE_URL + "/" + REST_1_ID)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._links." + SELF_VALUE).exists())
                .andExpect(jsonPath("$._links." + COLLECTION_VALUE).exists());
    }

    @Test
    void getTodayWithDishesAndVotes() throws Exception {
        get(BASE_URL)
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._links." + SELF_VALUE).exists())
                .andExpect(jsonPath("$._embedded." + RestaurantWithDishesDto.COLLECTION).isArray());
    }

    @Test
    void voting() throws Exception {
        LocalDate today = DateTimeUtil.currentDate();
        int count = voteRepository.findByRestaurantAndVoteDate(REST_1, today).size();

        post(BASE_URL + "/" + REST_1_ID).andExpect(status().isNoContent());
        assertThat(voteRepository.findByRestaurantAndVoteDate(REST_1, today)).hasSize(count + 1);
    }
}