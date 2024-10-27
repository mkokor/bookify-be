package com.bookify.service.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;

@Configuration
@RequiredArgsConstructor
public class AuthenticationManagerConfig {

  private final AuthenticationProvider authenticationProvider;

  @Bean
  public AuthenticationManager authenticationManager() {
    return new ProviderManager(authenticationProvider);
  }

}