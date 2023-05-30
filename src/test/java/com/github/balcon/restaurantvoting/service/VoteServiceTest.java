package com.github.balcon.restaurantvoting.service;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.github.balcon.restaurantvoting.util.DateTimeUtil.currentDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class VoteServiceTest {
    @Autowired
    VoteRepository repository;

    @Autowired
    VoteService service;

    @Test
    void voteFirstTime() {
        service.vote(TestData.REST_1, TestData.USER);

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

            service.vote(TestData.REST_2, TestData.ADMIN);
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

            assertThrows(IllegalRequestException.class, () -> service.vote(TestData.REST_2, TestData.ADMIN));
        }
    }
}