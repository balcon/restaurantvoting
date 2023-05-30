package com.github.balcon.restaurantvoting.config;

import com.github.balcon.restaurantvoting.model.Role;
import com.github.balcon.restaurantvoting.model.User;
import com.github.balcon.restaurantvoting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.github.balcon.restaurantvoting.web.AuthUser;

import java.util.Optional;

import static com.github.balcon.restaurantvoting.config.AppConfig.API_URL;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final UserRepository repository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            Optional<User> optionalUser = repository.findByEmailIgnoreCase(email);
            AuthUser authUser = new AuthUser(optionalUser.orElseThrow(
                    () -> new UsernameNotFoundException("User " + email + " not found")));
            log.info("User [{}] authenticated", email);
            return authUser;
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .requestMatchers(API_URL + "/admin/**").hasRole(Role.ADMIN.name())
                .requestMatchers(HttpMethod.POST, API_URL + "/user/profile").anonymous()
                .requestMatchers(API_URL).permitAll()
                .requestMatchers(API_URL + "/**").hasRole(Role.USER.name())
                .requestMatchers("/**").permitAll()
                .and().httpBasic()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().csrf().disable();
        return http.build();
    }
}