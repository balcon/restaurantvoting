package ru.javaops.balykov.restaurantvoting.web.rest.user;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.javaops.balykov.restaurantvoting.dto.UserDto;
import ru.javaops.balykov.restaurantvoting.dto.assembler.ProfileAssembler;
import ru.javaops.balykov.restaurantvoting.model.User;
import ru.javaops.balykov.restaurantvoting.repository.UserRepository;
import ru.javaops.balykov.restaurantvoting.util.UserPreparator;
import ru.javaops.balykov.restaurantvoting.validation.EmailUniqueValidator;
import ru.javaops.balykov.restaurantvoting.validation.ValidationUtil;
import ru.javaops.balykov.restaurantvoting.web.AuthUser;
import ru.javaops.balykov.restaurantvoting.web.rest.BaseController;

import static ru.javaops.balykov.restaurantvoting.config.AppConfig.API_URL;

@RestController
@RequestMapping(ProfileController.BASE_URL)
@Slf4j
public class ProfileController extends BaseController<User> {
    protected static final String BASE_URL = API_URL + "/user/profile";

    private final UserRepository repository;
    private final EmailUniqueValidator validator;
    private final UserPreparator preparator;
    private final ProfileAssembler assembler;

    public ProfileController(UserRepository repository, EmailUniqueValidator validator,
                             UserPreparator preparator, ProfileAssembler assembler) {
        super(repository, log);
        this.repository = repository;
        this.validator = validator;
        this.preparator = preparator;
        this.assembler = assembler;
    }

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody User user) {
        return assembler.toModel(super.doCreate(preparator.prepareToSave(user)));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDto getAuth(@AuthenticationPrincipal AuthUser authUser) {
        return assembler.toModel(authUser.getUser());
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody User user,
                       @AuthenticationPrincipal AuthUser authUser) throws BindException {
        log.info("Update profile [{}] to [{}]", authUser, user);
        ValidationUtil.assureIdConsistent(user, authUser.id());

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