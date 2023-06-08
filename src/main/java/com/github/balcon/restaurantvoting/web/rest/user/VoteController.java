package com.github.balcon.restaurantvoting.web.rest.user;

import com.github.balcon.restaurantvoting.model.Vote;
import com.github.balcon.restaurantvoting.service.VoteService;
import com.github.balcon.restaurantvoting.util.DateTimeUtil;
import com.github.balcon.restaurantvoting.web.AuthUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

import static com.github.balcon.restaurantvoting.config.AppConfig.API_URL;

@RestController
@RequestMapping(VoteController.BASE_URL)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vote controller", description = "Vote for restaurant and get users votes")
public class VoteController {
    protected static final String BASE_URL = API_URL + "/user/vote";

    private final VoteService service;

    @GetMapping
    public ResponseEntity<Vote> getVotes(@AuthenticationPrincipal AuthUser authUser,
                                         @RequestParam(required = false) Optional<LocalDate> voteDate) {
        LocalDate date = voteDate.orElseGet(DateTimeUtil::currentDate);
        log.info("Getting votes of [{}] for date [{}]", authUser.getUser(), date);
        return ResponseEntity.of(service.get(authUser.getUser(), date));
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void vote(@AuthenticationPrincipal AuthUser authUser,
                     @RequestParam int restaurantId) {
        log.info("Voting by [{}] for restaurant id: [{}]", authUser.getUser(), restaurantId);
        service.doVote(restaurantId, authUser.getUser());
    }
}