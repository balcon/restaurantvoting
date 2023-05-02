package ru.javaops.balykov.restaurantvoting.web.rest.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.javaops.balykov.restaurantvoting.dto.RestaurantDto;
import ru.javaops.balykov.restaurantvoting.dto.assembler.RestaurantAssembler;
import ru.javaops.balykov.restaurantvoting.model.Restaurant;
import ru.javaops.balykov.restaurantvoting.repository.RestaurantRepository;
import ru.javaops.balykov.restaurantvoting.validation.RestaurantUniqueValidator;
import ru.javaops.balykov.restaurantvoting.web.rest.BaseController;
import ru.javaops.balykov.restaurantvoting.web.rest.HalLinkMethods;

import static ru.javaops.balykov.restaurantvoting.config.AppConfig.API_URL;

@RestController
@RequestMapping(RestaurantController.BASE_URL)
@Slf4j
@Tag(name = "Restaurant controller", description = "CRUD operations for restaurants")
public class RestaurantController extends BaseController<Restaurant> implements HalLinkMethods {
    protected static final String BASE_URL = API_URL + "/admin/restaurants";

    private final RestaurantUniqueValidator validator;
    private final RestaurantAssembler assembler;

    public RestaurantController(RestaurantRepository repository, RestaurantUniqueValidator validator,
                                RestaurantAssembler assembler) {
        super(repository, log);
        this.validator = validator;
        this.assembler = assembler;
    }

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(validator);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantDto create(@Valid @RequestBody Restaurant restaurant) {
        return assembler.toModelWithCollection(super.doCreate(restaurant));
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public RestaurantDto getById(@PathVariable int id) {
        return assembler.toModelWithCollection(super.doGetById(id));
    }

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Override
    public CollectionModel<RestaurantDto> getAll(@SortDefault(sort = {"name", "address"})
                                                 @ParameterObject Pageable pageable) {
        return assembler.toCollectionModel(super.doGetAll(pageable));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody Restaurant restaurant) {
        super.doUpdate(id, restaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.doDelete(id);
    }
}