package com.github.balcon.restaurantvoting.web.rest.user;

import com.github.balcon.restaurantvoting.dto.RestaurantWithDishesDto;
import com.github.balcon.restaurantvoting.web.rest.BaseMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.security.test.context.support.WithUserDetails;

import static com.github.balcon.restaurantvoting.util.TestData.*;
import static com.github.balcon.restaurantvoting.web.rest.user.RestaurantUserController.BASE_URL;
import static org.springframework.hateoas.IanaLinkRelations.COLLECTION_VALUE;
import static org.springframework.hateoas.IanaLinkRelations.SELF_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithUserDetails(USER_EMAIL)
class RestaurantUserControllerTest extends BaseMvcTest {
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
}