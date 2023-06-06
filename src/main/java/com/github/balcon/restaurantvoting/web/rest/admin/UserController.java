package com.github.balcon.restaurantvoting.web.rest.admin;

import com.github.balcon.restaurantvoting.dto.UserDto;
import com.github.balcon.restaurantvoting.dto.assembler.MethodsForAssembler;
import com.github.balcon.restaurantvoting.dto.assembler.UserAssembler;
import com.github.balcon.restaurantvoting.model.User;
import com.github.balcon.restaurantvoting.service.UserService;
import com.github.balcon.restaurantvoting.util.validation.EmailUniqueValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User controller", description = "CRUD operations for users")
public class UserController implements MethodsForAssembler {
    public static final String BASE_URL = API_URL + "/admin/users";

    private final UserService service;
    private final EmailUniqueValidator emailUniqueValidator;
    private final UserAssembler assembler;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(emailUniqueValidator);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registration new user")
    public UserDto create(@Valid @RequestBody User user) {
        return assembler.toModelWithCollection(service.create(user));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public UserDto getById(@PathVariable int id) {
        return assembler.toModelWithCollection(service.get(id));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Override
    public CollectionModel<UserDto> getAll(@SortDefault(sort = {"name", "email"})
                                           @ParameterObject Pageable pageable) {
        return assembler.toCollectionModel(service.getAll(pageable));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @RequestBody User user) throws BindException {
        service.update(id, user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        service.delete(id);
    }
}