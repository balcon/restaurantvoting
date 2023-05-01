package ru.javaops.balykov.restaurantvoting.web.rest.admin;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.javaops.balykov.restaurantvoting.dto.UserDto;
import ru.javaops.balykov.restaurantvoting.dto.assembler.UserDtoAssembler;
import ru.javaops.balykov.restaurantvoting.model.User;
import ru.javaops.balykov.restaurantvoting.repository.UserRepository;
import ru.javaops.balykov.restaurantvoting.util.UserPreparator;
import ru.javaops.balykov.restaurantvoting.validation.EmailUniqueValidator;
import ru.javaops.balykov.restaurantvoting.validation.ValidationUtil;
import ru.javaops.balykov.restaurantvoting.web.rest.BaseController;
import ru.javaops.balykov.restaurantvoting.web.rest.HalLinksMethods;

import static ru.javaops.balykov.restaurantvoting.config.AppConfig.API_URL;

@RestController
@RequestMapping(UserController.BASE_URL)
@Slf4j
public class UserController extends BaseController<User> implements HalLinksMethods {
    public static final String BASE_URL = API_URL + "/admin/users";

    private final UserRepository repository;
    private final EmailUniqueValidator validator;
    private final UserPreparator preparator;
    private final UserDtoAssembler assembler;

    public UserController(UserRepository repository, EmailUniqueValidator validator,
                          UserPreparator preparator, UserDtoAssembler assembler) {
        super(repository, log);
        this.repository = repository;
        this.preparator = preparator;
        this.validator = validator;
        this.assembler = assembler;
    }

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        return super.create(preparator.prepareToSave(user));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getById(@PathVariable int id) {
        return assembler.toModel(super.baseGetById(id));
    }

    //    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
//    public Page<User> baseGetAll(@SortDefault(sort = {"name", "email"}) Pageable pageable) {
//        return super.baseGetAll(pageable);
//    }//
//
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<UserDto> getAll(@SortDefault(sort = {"name", "email"}) Pageable pageable) {
        return assembler.toCollectionModel(super.baseGetAll(pageable));
    }

    @PutMapping("/{id}")
//    @Transactional
    // TODO: 30.04.2023 transactional breaks custom validatord 
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody User user, @PathVariable int id) throws BindException {
        log.info("Update [{}] with id [{}]", user, id);
        ValidationUtil.assureIdConsistent(user, id);
        repository.save(preparator.prepareToUpdate(user, id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }
}