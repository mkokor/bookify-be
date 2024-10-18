package com.bookify.rest.authentication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationRestService {

  @GetMapping("/sign-in")
  public String signIn() {
    return "Hello, World!";
  }
  
}