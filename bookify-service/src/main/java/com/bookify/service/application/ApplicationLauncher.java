package com.bookify.service.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.bookify")
@EntityScan(basePackages = "com.bookify.dao.model")
public class ApplicationLauncher {

  public static void main(String[] args) {
    SpringApplication.run(ApplicationLauncher.class, args);
  }

}