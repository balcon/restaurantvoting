package ru.javaops.balykov.restaurantvoting.service;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.javaops.balykov.restaurantvoting.model.Vote;
import ru.javaops.balykov.restaurantvoting.repository.VoteRepository;
import ru.javaops.balykov.restaurantvoting.util.DateTimeUtil;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javaops.balykov.restaurantvoting.util.DateTimeUtil.currentDate;
import static ru.javaops.balykov.restaurantvoting.util.TestData.*;

@SpringBootTest
@Transactional
class VoteServiceTest {
    @Autowired
    VoteRepository repository;

    @Autowired
    VoteService service;

    @Test
    void voteFirstTime() {
        service.vote(REST_1, ADMIN);

        assertThat(repository.existsByUserAndVoteDate(ADMIN, currentDate())).isTrue();
    }

    @Test
    void legalRevote() {
        LocalDate today = currentDate();
        try (MockedStatic<DateTimeUtil> dateTimeUtilMocked = Mockito.mockStatic(DateTimeUtil.class)) {
            dateTimeUtilMocked.when(DateTimeUtil::currentTime)
                    .thenReturn(DateTimeUtil.REVOTE_DEADLINE.minusHours(1));
            dateTimeUtilMocked.when(DateTimeUtil::currentDate)
                    .thenReturn(today);

            service.vote(REST_2, USER);
            Vote vote = repository.findByUserAndVoteDate(USER, today).orElseThrow();

            assertThat(vote.getRestaurant()).isEqualTo(REST_2);
        }
    }

    @Test
    void illegalRevote() {
        LocalDate today = currentDate();
        try (MockedStatic<DateTimeUtil> dateTimeUtilMocked = Mockito.mockStatic(DateTimeUtil.class)) {
            dateTimeUtilMocked.when(DateTimeUtil::currentTime)
                    .thenReturn(DateTimeUtil.REVOTE_DEADLINE.plusHours(1));
            dateTimeUtilMocked.when(DateTimeUtil::currentDate)
                    .thenReturn(today);

            assertThrows(IllegalStateException.class, () -> service.vote(REST_2, USER));
        }
    }
}