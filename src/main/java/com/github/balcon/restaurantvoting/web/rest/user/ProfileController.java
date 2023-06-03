package com.github.balcon.restaurantvoting.web.rest.user;

import com.github.balcon.restaurantvoting.dto.UserDto;
import com.github.balcon.restaurantvoting.dto.assembler.ProfileAssembler;
import com.github.balcon.restaurantvoting.model.User;
import com.github.balcon.restaurantvoting.repository.UserRepository;
import com.github.balcon.restaurantvoting.util.UserPreparator;
import com.github.balcon.restaurantvoting.util.UserUpdateRestrictor;
import com.github.balcon.restaurantvoting.util.validation.EmailUniqueValidator;
import com.github.balcon.restaurantvoting.util.validation.ValidationUtil;
import com.github.balcon.restaurantvoting.web.AuthUser;
import com.github.balcon.restaurantvoting.web.rest.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Slf4j
@Tag(name = "Profile controller", description = "Get, update and delete profile. Registration new user")
public class ProfileController extends BaseController<User> {
    protected static final String BASE_URL = API_URL + "/user/profile";

    private final UserRepository repository;
    private final EmailUniqueValidator validator;
    private final UserPreparator preparator;
    private final ProfileAssembler assembler;
    private final UserUpdateRestrictor restrictor;

    public ProfileController(UserRepository repository, EmailUniqueValidator validator,
                             UserPreparator preparator, ProfileAssembler assembler, UserUpdateRestrictor restrictor) {
        super(repository, log);
        this.repository = repository;
        this.validator = validator;
        this.preparator = preparator;
        this.assembler = assembler;
        this.restrictor = restrictor;
    }

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registration new user")
    public UserDto create(@Valid @RequestBody User user) {
        return assembler.toModel(super.doCreate(preparator.prepareToSave(user)));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get authenticated user")
    public UserDto getAuth(@AuthenticationPrincipal AuthUser authUser) {
        return assembler.toModel(authUser.getUser());
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody User user,
                       @AuthenticationPrincipal AuthUser authUser) throws BindException {
        log.info("Update profile [{}] to [{}]", authUser, user);
        ValidationUtil.assureIdConsistent(user, authUser.id());
        restrictor.check(authUser.id());
        // User does not have to change their roles
        user.setRoles(authUser.getUser().getRoles());
        repository.save(preparator.prepareToUpdate(user, authUser.getUser()));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        super.doDelete(authUser.id());
    }
}