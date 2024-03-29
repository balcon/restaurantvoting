package com.github.balcon.restaurantvoting.service;

import com.github.balcon.restaurantvoting.BaseTest;
import com.github.balcon.restaurantvoting.exception.IllegalRequestException;
import com.github.balcon.restaurantvoting.model.Vote;
import com.github.balcon.restaurantvoting.repository.VoteRepository;
import com.github.balcon.restaurantvoting.util.DateTimeUtil;
import com.github.balcon.restaurantvoting.util.TestData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static com.github.balcon.restaurantvoting.util.DateTimeUtil.currentDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VoteServiceTest extends BaseTest {
    @Autowired
    VoteRepository repository;

    @Autowired
    VoteService service;

    @Test
    void voteFirstTime() {
        service.doVote(TestData.REST_1_ID, TestData.USER);

        Assertions.assertThat(repository.findByUserAndVoteDate(TestData.USER, currentDate())).isPresent();
    }

    @Test
    void legalRevote() {
        LocalDate today = currentDate();
        try (MockedStatic<DateTimeUtil> dateTimeUtilMocked = Mockito.mockStatic(DateTimeUtil.class)) {
            dateTimeUtilMocked.when(DateTimeUtil::currentTime)
                    .thenReturn(VoteService.REVOTE_DEADLINE.minusHours(1));
            dateTimeUtilMocked.when(DateTimeUtil::currentDate)
                    .thenReturn(today);

            service.doVote(TestData.REST_2_ID, TestData.ADMIN);
            Vote vote = repository.findByUserAndVoteDate(TestData.ADMIN, today).orElseThrow();

            assertThat(vote.getRestaurant()).isEqualTo(TestData.REST_2);
        }
    }

    @Test
    void illegalRevote() {
        LocalDate today = currentDate();
        try (MockedStatic<DateTimeUtil> dateTimeUtilMocked = Mockito.mockStatic(DateTimeUtil.class)) {
            dateTimeUtilMocked.when(DateTimeUtil::currentTime)
                    .thenReturn(VoteService.REVOTE_DEADLINE.plusHours(1));
            dateTimeUtilMocked.when(DateTimeUtil::currentDate)
                    .thenReturn(today);

            assertThrows(IllegalRequestException.class, () -> service.doVote(TestData.REST_2_ID, TestData.ADMIN));
        }
    }
}