package com.bookify.service.configuration.security;

import static org.springframework.security.config.Customizer.withDefaults;

import com.bookify.api.enums.Role;
import com.bookify.service.filter.AccessTokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

  private final AccessTokenAuthenticationFilter accessTokenAuthenticationFilter;

  @Bean
  public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
    http.cors(withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(req ->
            req.requestMatchers("/book/authorization-employee")
                .hasAuthority(Role.EMPLOYEE.getValue())
                .requestMatchers("/book/authorization-customer")
                .hasAuthority(Role.CUSTOMER.getValue())
                .anyRequest().permitAll()
        ).sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(accessTokenAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

}