package com.bookify.service.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EntityScan(basePackages = "com.bookify.dao.model")
@EnableJpaRepositories("com.bookify.dao.repository")
@ComponentScan(basePackages = "com.bookify")
public class ApplicationLauncher {

  public static void main(String[] args) {
    SpringApplication.run(ApplicationLauncher.class, args);
  }

}