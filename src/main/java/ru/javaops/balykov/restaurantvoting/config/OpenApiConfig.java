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
                version = "0.9.0-SNAPSHOT",
                description = """
                        <h4><a href='https://topjava.ru/topjava'>Java course</a> graduation project.</h4>
                        <h4>Technical requirement:</h4>
                        <p>Design and implement a REST API using Hibernate/Spring/SpringMVC (Spring-Boot preferred!) without frontend.</p>
                        <p>The task is:</p>
                        <p>Build a voting system for deciding where to have lunch.</p>
                        <ul>
                            <li>2 types of users: admin and regular users</li>
                            <li>Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)</li>
                            <li>Menu changes each day (admins do the updates)</li>
                            <li>Users can vote for a restaurant they want to have lunch at today</li>
                            <li>Only one vote counted per user</li>
                            <li>If user votes again the same day:</li>
                            <ul>
                                <li>If it is before 11:00 we assume that he changed his mind.</li>
                                <li>If it is after 11:00 then it is too late, vote can't be changed</li>
                            </ul>
                            <li>Each restaurant provides a new menu each day.</li>
                        <ul>
                        <p><b>Тестовые креденшелы:</b><br>
                        - user@gmail.com / password<br>
                        - admin@javaops.ru / admin</p>
                        """,
                contact = @Contact(
                        url = "https://github.com/balcon",
                        name = "Konstantin Balykov")
        ),
        security = @SecurityRequirement(name = "basicAuth")
)
public class OpenApiConfig {
    @Bean
    public GroupedOpenApi restApi() {
        return GroupedOpenApi.builder()
                .group("REST API")
                .pathsToMatch(AppConfig.API_URL + "/**")
                .build();
    }
}