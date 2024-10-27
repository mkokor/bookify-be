package com.bookify.service.configuration.email;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource("classpath:email.properties")
public class EmailConfig {

  @Value("${spring.mail.host}")
  private String host;

  @Value("${spring.mail.port}")
  private Integer port;

  @Value("${spring.mail.username}")
  private String username;

  @Value("${spring.mail.password}")
  private String password;

  @Value("${spring.mail.properties.mail.smtp.auth}")
  private Boolean smtpAuth;

  @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
  private Boolean tlsEnabled;

  @Bean
  public JavaMailSender javaMailSender() {
    final JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
    javaMailSender.setHost(host);
    javaMailSender.setPort(port);
    javaMailSender.setUsername(username);
    javaMailSender.setPassword(password);

    final Properties properties = javaMailSender.getJavaMailProperties();
    properties.put("mail.smtp.auth", smtpAuth.toString());
    properties.put("mail.smtp.starttls.enable", tlsEnabled.toString());

    return javaMailSender;
  }

}