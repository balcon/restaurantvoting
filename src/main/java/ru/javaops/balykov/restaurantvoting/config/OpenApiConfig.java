package ru.javaops.balykov.restaurantvoting.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//https://sabljakovich.medium.com/adding-basic-auth-authorization-option-to-openapi-swagger-documentation-java-spring-95abbede27e9
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
@OpenAPIDefinition(
        info = @Info(
                title = "Restaurants voting REST app.",
                version = "0.9.1",
                description = """
                        <h4><a href='https://topjava.ru/topjava'>Java course</a> graduation project.</h4>
                        <h4><a href='https://github.com/balcon/restaurantvoting'>Technical requirement and credentials.</a></h4>
                        """,
                contact = @Contact(
                        url = "https://github.com/balcon",
                        name = "Konstantin Balykov")
        ),
        security = @SecurityRequirement(name = "basicAuth")
)
public class OpenApiConfig {
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("Admin API")
                .pathsToMatch(AppConfig.API_URL + "/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("User API")
                .pathsToMatch(AppConfig.API_URL + "/user/**")
                .build();
    }
}