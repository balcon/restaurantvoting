package com.github.balcon.restaurantvoting.web.rest.user;

import com.github.balcon.restaurantvoting.dto.UserDto;
import com.github.balcon.restaurantvoting.dto.assembler.ProfileAssembler;
import com.github.balcon.restaurantvoting.model.User;
import com.github.balcon.restaurantvoting.service.UserService;
import com.github.balcon.restaurantvoting.util.validation.EmailUniqueValidator;
import com.github.balcon.restaurantvoting.web.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import static com.github.balcon.restaurantvoting.config.AppConfig.API_URL;

@RestController
@RequestMapping(path = ProfileController.BASE_URL, produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Profile controller", description = "Get, update and delete profile. Registration new user")
public class ProfileController {
    protected static final String BASE_URL = API_URL + "/user/profile";

    private final UserService service;
    private final EmailUniqueValidator emailUniqueValidator;
    private final ProfileAssembler assembler;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(emailUniqueValidator);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registration new user")
    public UserDto create(@Valid @RequestBody User user) {
        log.info("Registering new [{}]", user);
        return assembler.toModel(service.create(user));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get authenticated user")
    public UserDto getAuth(@AuthenticationPrincipal AuthUser authUser) {
        log.info("Getting authenticated [{}]", authUser.getUser());
        return assembler.toModel(authUser.getUser());
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser,
                       @RequestBody User user) throws BindException {
        log.info("Updating profile [{}] to [{}]", authUser.getUser(), user);
        // User does not have to change their roles
        user.setRoles(authUser.getUser().getRoles());
        service.update(authUser.id(), user);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        log.info("Deleting profile [{}]", authUser.getUser());
        service.delete(authUser.id());
    }
}