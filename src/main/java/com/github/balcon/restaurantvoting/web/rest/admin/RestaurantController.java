package com.github.balcon.restaurantvoting.web.rest.admin;

import com.github.balcon.restaurantvoting.dto.RestaurantDto;
import com.github.balcon.restaurantvoting.dto.assembler.MethodsForAssembler;
import com.github.balcon.restaurantvoting.dto.assembler.RestaurantAssembler;
import com.github.balcon.restaurantvoting.model.Restaurant;
import com.github.balcon.restaurantvoting.service.RestaurantService;
import com.github.balcon.restaurantvoting.util.validation.RestaurantUniqueValidator;
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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import static com.github.balcon.restaurantvoting.config.AppConfig.API_URL;

@RestController
@RequestMapping(path = RestaurantController.BASE_URL, produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Restaurant controller", description = "CRUD operations for restaurants")
public class RestaurantController implements MethodsForAssembler {
    protected static final String BASE_URL = API_URL + "/admin/restaurants";

    private final RestaurantService service;
    private final RestaurantUniqueValidator restaurantUniqueValidator;
    private final RestaurantAssembler assembler;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.addValidators(restaurantUniqueValidator);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestaurantDto create(@Valid @RequestBody Restaurant restaurant) {
        return assembler.toModelWithCollection(service.create(restaurant));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Override
    public RestaurantDto getById(@PathVariable int id) {
        return assembler.toModelWithCollection(service.get(id));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Override
    public CollectionModel<RestaurantDto> getAll(@SortDefault(sort = {"name", "address"})
                                                 @ParameterObject Pageable pageable) {
        return assembler.toCollectionModel(service.getAll(pageable));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable int id, @Valid @RequestBody Restaurant restaurant) {
        service.update(id, restaurant);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        service.delete(id);
    }
}