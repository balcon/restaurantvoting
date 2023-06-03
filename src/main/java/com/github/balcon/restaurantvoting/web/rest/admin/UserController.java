package com.github.balcon.restaurantvoting.web.rest.admin;

import com.github.balcon.restaurantvoting.dto.UserDto;
import com.github.balcon.restaurantvoting.dto.assembler.MethodsForAssembler;
import com.github.balcon.restaurantvoting.dto.assembler.UserAssembler;
import com.github.balcon.restaurantvoting.model.User;
import com.github.balcon.restaurantvoting.repository.UserRepository;
import com.github.balcon.restaurantvoting.util.UserPreparator;
import com.github.balcon.restaurantvoting.util.UserUpdateRestrictor;
import com.github.balcon.restaurantvoting.util.validation.EmailUniqueValidator;
import com.github.balcon.restaurantvoting.util.validation.ValidationUtil;
import com.github.balcon.restaurantvoting.web.rest.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import static com.github.balcon.restaurantvoting.config.AppConfig.API_URL;

@RestController
@RequestMapping(path = UserController.BASE_URL, produces = MediaTypes.HAL_JSON_VALUE)
@Slf4j
@Tag(name = "User controller", description = "CRUD operations for users")
public class UserController extends BaseController<User> implements MethodsForAssembler {
    public static final String BASE_URL = API_URL + "/admin/users";

    private final UserRepository repository;
    private final EmailUniqueValidator validator;
    private final UserPreparator preparator;
    private final UserAssembler assembler;
    private final UserUpdateRestrictor restrictor;

    public UserController(UserRepository repository, EmailUniqueValidator validator,
                          UserPreparator preparator, UserAssembler assembler, UserUpdateRestrictor restrictor) {
        super(repository, log);
        this.repository = repository;
        this.preparator = preparator;
        this.validator = validator;
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
        return assembler.toModelWithCollection(super.doCreate(preparator.prepareToSave(user)));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public UserDto getById(@PathVariable int id) {
        return assembler.toModelWithCollection(super.doGetById(id));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Override
    public CollectionModel<UserDto> getAll(@SortDefault(sort = {"name", "email"})
                                           @ParameterObject Pageable pageable) {
        return assembler.toCollectionModel(super.doGetAll(pageable));
    }

    @PutMapping("/{id}")
    // @Transactional
    // Transaction breaks custom validators
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @RequestBody User user) throws BindException {
        log.info("Update [{}] with id [{}]", user, id);
        restrictor.check(id);
        ValidationUtil.assureIdConsistent(user, id);
        repository.save(preparator.prepareToUpdate(user, id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.doDelete(id);
    }
}